import os
from langchain_google_genai import ChatGoogleGenerativeAI, GoogleGenerativeAIEmbeddings
from dotenv import load_dotenv
from langchain_community.vectorstores import PGVector
from langchain_core.documents import Document
from langchain_core.tools import tool


# GraphBuilder.py

import os
import sys
import uuid
from typing import Literal, List, Annotated
from typing_extensions import TypedDict

sys.path.append(os.path.abspath("/home/lumasty/Documents/GitHub/MAFinancialAssistance"))
from DBAgent.Querier import Querier
# --- Environment and API Keys ---
from dotenv import load_dotenv
load_dotenv(override=True) # Load environment variables from .env file

# --- LangChain & LangGraph Imports ---
from langchain_core.messages import HumanMessage, BaseMessage, ToolMessage, AIMessage
from langchain_google_genai import ChatGoogleGenerativeAI
# from langchain_ollama import ChatOllama # Alternative LLM
from langgraph.graph import StateGraph, START, END, MessagesState
from langgraph.graph.message import add_messages
from langgraph.prebuilt import create_react_agent # For researcher
from langgraph.types import Command

# --- Tool Imports ---
from langchain_community.tools.tavily_search import TavilySearchResults

# --- Pydantic for Structured Output ---
from pydantic import BaseModel, Field

# --- Streamlit for UI ---
import streamlit as st


# --- Configuration ---

# Setup LLM (Using Gemini as per the GraphBuilder snippet)
try:
    base_model = os.getenv("MODEL", "gemini-pro") # Default to gemini-pro if not set
    base_api = os.getenv("BASE_API")
    if not base_api:
        raise ValueError("BASE_API environment variable not set for Google API Key.")
    llm = ChatGoogleGenerativeAI(model=base_model, google_api_key=base_api, temperature=0)
    llm.invoke("Hi") # Test initialization
    print(f"Google Generative AI LLM ({base_model}) initialized.")
except Exception as e:
    st.error(f"Failed to initialize Google Generative AI LLM: {e}")
    sys.exit("LLM initialization failed.")


# --- Tools ---
try:
    tavily_api_key = os.environ["TAVILY_API_KEY"]
    tavily_tool = TavilySearchResults(max_results=3)
except KeyError:
    st.warning("TAVILY_API_KEY environment variable not set. Researcher agent will be disabled.")
    tavily_tool = None

# --- Initialize Querier ---
# Pass the initialized LLM to the Querier if it needs it
try:
    querier = Querier(llm)
except Exception as e:
    st.error(f"Failed to initialize Querier: {e}")
    sys.exit(1)
    
# Setup Embeddings
try:
    embedding_model_name = os.getenv("EMBEDDING_MODEL", "models/embedding-001")
    embeddings = GoogleGenerativeAIEmbeddings(model=embedding_model_name, google_api_key=base_api)
    print(f"Google Generative AI Embeddings ({embedding_model_name}) initialized.")
except Exception as e:
    st.error(f"Failed to initialize Google Embeddings: {e}")
    sys.exit("Embeddings initialization failed.")
    
# --- Initialize PGVector Memory Store ---
PGVECTOR_CONNECTION_STRING = os.getenv("PGVECTOR_CONNECTION_STRING")
# print(PGVECTOR_CONNECTION_STRING)
COLLECTION_NAME = "user_recall_memories_v2" # Use a distinct name

try:
    recall_vector_store = PGVector(
        connection_string=PGVECTOR_CONNECTION_STRING,
        embedding_function=embeddings,
        collection_name=COLLECTION_NAME,
    )
    print(f"PGVector connected/initialized for collection '{COLLECTION_NAME}'.")
    # Add check for vector extension if needed (see previous example)
except Exception as e:
    st.error(f"Failed to initialize PGVector: {e}")
    st.error("Ensure PostgreSQL server is running, accessible, and the 'vector' extension is enabled.")
    sys.exit(1)
    

# --- Memory Tools Definition ---
@tool
def save_recall_memory(memory_content: str) -> str:
    """Save a recall memory about the user to PGVector."""
    user_id = st.session_state.user_id
    doc_id = str(uuid.uuid4())
    document = Document(
        page_content=memory_content,
        metadata={"user_id": user_id, "doc_id": doc_id, "created_at": str(uuid.uuid4())}
    )
    try:
        recall_vector_store.add_documents([document])
        print(f"Saved memory to PGVector for user {user_id}: '{memory_content}'")
        return f"Successfully saved memory: {memory_content}"
    except Exception as e:
        print(f"Error saving memory to PGVector for user {user_id}: {e}")
        return f"Error saving memory: {e}"

@tool
def search_recall_memories(query: str) -> List[str]:
    """Search recall memories for the current user using PGVector."""
    user_id = st.session_state.user_id
    try:
        results = recall_vector_store.similarity_search(
           query,
           k=3, # Retrieve top 3
           filter={"user_id": user_id} # Use PGVector metadata filtering
        )
        user_results = [doc.page_content for doc in results]
        print(f"Found {len(user_results)} memories via PGVector for user {user_id} matching query '{query}'")
        return user_results
    except Exception as e:
        print(f"Error searching PGVector memory for user {user_id}: {e}")
        return [f"Error searching memories: {e}"]

# --- Supervisor Definition ---

members = ["querier", "researcher"]
options = members + ["FINISH"]

# Enhanced System Prompt for CoT Supervisor (Updated for Memory)
system_prompt = (
    f"""You are a supervisor managing a conversation between the following workers: {members}.
    'querier' handles PostgreSQL database interactions for specific financial data.
    'researcher' performs internet searches for broader financial topics or current information using Tavily Search.

    **Important Context:** Relevant memories about the user have been retrieved and are provided below. Use these memories to better understand the user's context, history, and preferences when making decisions.

    Given the conversation history (including user requests, worker results, and retrieved memories), your goal is to decide the next best step to fully address the user's overall request.

    First, provide your step-by-step 'reasoning' process:
    1.  Briefly summarize the original user request and the current goal.
    2.  **Review the provided 'Retrieved User Memories'. How do they relate to the current request?**
    3.  Review the actions taken so far by 'querier' and 'researcher' and their key findings based on the message history.
    4.  Evaluate if the information gathered so far (including memories and worker results) sufficiently answers the user's request.
    5.  If not answered, determine which worker ('querier' or 'researcher') is best suited for the *next* step to get closer to the goal. Consider if specific database data is missing, if memories provide sufficient context, or if broader context/external info is needed. Explain why.
    6.  If the request *is* sufficiently answered, explain why and conclude that the next step is 'FINISH'.

    After detailing your 'reasoning', state the final 'next' action ('querier', 'researcher', or 'FINISH') using the provided schema."""
)

# --- Graph State ---
class State(TypedDict):
    messages: Annotated[List[BaseMessage], add_messages]
    next: str
    queried_this_cycle: bool
    searched_this_cycle: bool
    recall_memories: List[str]
    current_task_prompt: str
    # Optional: Add reasoning to state if needed elsewhere, but for this
    # approach, we inject it into the next task prompt instead.
    # supervisor_reasoning: Optional[str] = None

# --- Pydantic Schema for Supervisor's CoT Output (No change needed) ---
class SupervisorDecision(BaseModel):
    reasoning: str = Field(...)
    next: Literal[*options] = Field(...)

# --- GraphBuilder Class ---
class GraphBuilder():
    def __init__(self, llm, querier):
        self.supervisor_llm = llm
        self.querier = querier
        if tavily_tool:
            # Researcher prompt can be simpler now as context comes from supervisor
            researcher_prompt = "You are a web researcher. Execute the task based on the provided context using the Tavily Search tool."
            self.research_agent = create_react_agent(llm, tools=[tavily_tool], prompt=researcher_prompt)
            print("Researcher agent created.")
        else:
            self.research_agent = None
            print("Researcher agent NOT created.")
        self.query = ""

    # --- load_memories_node (Assumed Corrected As Per Previous Step) ---
    def load_memories_node(self, state: State) -> dict:
        # ... (logic to load memories based on last message content) ...
        # Returns {"recall_memories": relevant_memories}
        # Ensure it handles errors and returns list of strings
        st.markdown("---")
        st.info("(Memory Check)")
        relevant_memories = []
        query_string = None

        try:
            message_history = state.get("messages", [])
            if message_history:
                last_message = message_history[-1]
                if hasattr(last_message, 'content') and isinstance(last_message.content, str):
                    query_string = last_message.content
                    st.caption(f"Memory Query based on: '{query_string[:100]}...'")
                else: st.caption("Last message has no string content for memory query.")
            else: st.caption("No message history found for memory query.")

            if query_string:
                self.query = query_string
                relevant_memories = search_recall_memories.invoke({"query": query_string})
                if not isinstance(relevant_memories, list):
                     print(f"Warning: search_recall_memories did not return a list, got: {type(relevant_memories)}")
                     relevant_memories = [str(relevant_memories)] if relevant_memories else [] # Basic handling
            else: relevant_memories = []

        except Exception as e:
            st.warning(f"Memory search failed during invoke: {e}")
            if query_string: print(f"Failed memory search for query: {query_string}")
            relevant_memories = [f"Error loading memories during invoke: {e}"]

        if not isinstance(relevant_memories, list):
            relevant_memories = [f"Internal Error: memories not a list ({type(relevant_memories)})"]

        if relevant_memories and not any("Error" in str(mem) for mem in relevant_memories):
            with st.expander(f"Retrieved {len(relevant_memories)} Memories", expanded=False):
                for mem in relevant_memories: st.caption(f"- {mem}")
        elif not relevant_memories and query_string: st.caption("No relevant memories found.")

        return {"recall_memories": relevant_memories}


    # --- Supervisor Node (Injects Context into Task Prompt) ---
    def supervisor_node(self, state: State) -> Command[Literal[*members, END]]:
        st.markdown("---")
        st.info("Supervisor Turn")
        final_contextual_task = "" # Initialize

        with st.spinner("Supervisor thinking (considering memories)..."):
            current_messages = state.get("messages", [])
            loaded_memories = state.get("recall_memories", [])
            memory_context_str = "\n\n**Retrieved User Memories:**\n" + "\n".join(f"- {mem}" for mem in loaded_memories) if loaded_memories and not any("Error" in str(mem) for mem in loaded_memories) else "\n\n**Retrieved User Memories:**\n- None found or error during retrieval."

            messages_for_llm = [{"role": "system", "content": system_prompt + memory_context_str}] + current_messages

            try:
                structured_llm = self.supervisor_llm.with_structured_output(SupervisorDecision)
                response = structured_llm.invoke(messages_for_llm)
                reasoning = response.reasoning
                goto = response.next
                # Get the base task prompt decided by the LLM
                base_task_prompt = getattr(response, 'task_prompt', None) # Get base task if provided by schema, else None

                # If routing to a worker, construct the richer prompt
                if goto in members:
                    # Format memories nicely for the worker prompt
                    formatted_memories = "\n".join(f"  - {mem}" for mem in loaded_memories) if loaded_memories and not any("Error" in str(mem) for mem in loaded_memories) else "  - None"

                    # Construct the final contextual task prompt
                    final_contextual_task = f"""**Supervisor Context & Task:**
* **Goal/Reasoning:** {reasoning}
* **Current conversation:**
{st.session_state.short_memory}
* **Relevant Memories:**
{formatted_memories} 
* **Specific Task:** {base_task_prompt if base_task_prompt else self.query}

Please execute the 'Specific Task' using the provided reasoning and memories as context.
"""
                    st.markdown("---")
                    st.markdown(f"**Contextual Task for {goto}:**")
                    with st.expander("View Full Task", expanded=False):
                        st.markdown(final_contextual_task)
                    st.markdown("---")

            except Exception as e:
                st.error(f"Supervisor structured output/task generation failed: {e}")
                print(f"Supervisor Input: {messages_for_llm}")
                reasoning = f"Error: {e}"
                goto = "FINISH"

            # Display original reasoning
            with st.expander("Supervisor Reasoning", expanded=False):
                st.markdown(reasoning)
            st.markdown(f"**Supervisor Decision --> Route to: {goto}**")

            # --- Routing Logic ---
            if goto == "FINISH":
                page_content = f"**Question**: {self.query}\n**Answer**: {state.get('messages')[-1].content}"
                save_recall_memory.invoke({"memory_content": page_content})
                return Command(goto=END, update={"next": goto})

            # Loop prevention
            if goto == "querier" and state.get("queried_this_cycle"):
                st.warning("Loop detected: Querier -> Supervisor -> Querier. Forcing FINISH.")
                return Command(goto=END, update={"next": "FINISH"})
            elif goto == "researcher" and state.get("searched_this_cycle"):
                st.warning("Loop detected: Researcher -> Supervisor -> Researcher. Forcing FINISH.")
                return Command(goto=END, update={"next": "FINISH"})
            else:
                 # Update state with the final contextual task for the chosen worker
                return Command(
                    goto=goto,
                    update={
                        "next": goto,
                        "current_task_prompt": final_contextual_task, # <<< Use the enriched prompt
                        "queried_this_cycle": False, # Reset for next cycle
                        "searched_this_cycle": False # Reset for next cycle
                        }
                    )

    # --- Querier Node (Uses Contextual Task Prompt) ---
    def query_node(self, state: State) -> Command[Literal["supervisor"]]:
        st.markdown("---")
        st.info("Querier Turn")
        # --- Correction: Use 'current_task_prompt' from state ---
        contextual_task = state.get("current_task_prompt")
        # --- End Correction ---

        current_querier_ran_status = state.get('querier_ran', False) # Preserve status in case of error
        output_messages = []
        querier_ran_update = current_querier_ran_status

        if not contextual_task:
            st.warning("No contextual task found for Querier in state.")
            output_messages.append(AIMessage(content="Cannot query database: No task provided.", name="querier_error"))
            queried_this_cycle_update = True # Mark as attempted
        else:
            st.write(f"Executing Querier Task (using context)...") # Log what's sent
            with st.spinner("Querying database with context..."):
                try:
                    # Pass the full contextual task string to the querier
                    # Assumes querier.execute_query can handle natural language + context
                    result = self.querier.execute_query(contextual_task)

                    if isinstance(result, dict) and "messages" in result and result["messages"]:
                        response_message = result["messages"][-1]
                        if hasattr(response_message, 'content') and response_message.content is not None:
                            st.markdown("**Querier Result:**")
                            st.info(response_message.content)
                            output_messages.append(response_message) # Add the valid message
                        else:
                            st.warning("Querier returned message with no content.")
                            output_messages.append(AIMessage(content="[Querier returned message with no content]", name="querier_internal_error"))
                    else:
                        st.warning(f"Unexpected result format from Querier: {type(result)}. Using placeholder.")
                        output_messages.append(AIMessage(content="[Querier returned unexpected format]", name="querier_internal_error"))

                    querier_ran_update = True # Set flag on successful execution path
                    queried_this_cycle_update = True # Mark as ran in this cycle

                except Exception as e:
                    st.error(f"Querier execution failed: {e}")
                    output_messages.append(AIMessage(content=f"Error during Querier execution: {e}", name="querier_execution_error"))
                    queried_this_cycle_update = True # Mark as attempted even on error

        # Always return to supervisor after attempting
        update_dict = {
            "messages": output_messages,
            "querier_ran": querier_ran_update, # Keep track of overall runs (if needed)
            "queried_this_cycle": queried_this_cycle_update # Mark run for this cycle
        }
        return Command(update=update_dict, goto="supervisor")


    # --- Researcher Node (Uses Contextual Task Prompt) ---
    def research_node(self, state: State) -> Command[Literal["supervisor"]]:
        st.markdown("---")
        st.info("Researcher Turn")
        if not self.research_agent:
            # Return immediately if agent not available
            return Command(
                update={"messages": [AIMessage(content="Researcher unavailable.", name="researcher_error")], "searched_this_cycle": True}, # Mark as attempted
                goto="supervisor"
            )

        # --- Use 'current_task_prompt' from state ---
        contextual_task = state.get("current_task_prompt")
        # --- End Correction ---

        current_querier_ran_status = state.get('querier_ran', False) # Preserve if needed
        output_messages = []
        searched_this_cycle_update = False # Default to false

        if not contextual_task:
            st.warning("No contextual task found for Researcher in state.")
            output_messages.append(AIMessage(content="Cannot research: No task provided.", name="researcher_error"))
            searched_this_cycle_update = True # Mark as attempted
        else:
            st.write(f"Executing Researcher Task (using context)...")
            with st.spinner("Searching the web with context..."):
                try:
                     # --- Prepare input for React Agent ---
                     # Pass the contextual task as the primary human message content
                    agent_input = {"messages": [HumanMessage(content=contextual_task)]}

                    result = self.research_agent.invoke(agent_input) # Pass input dict

                    # Process result (remains similar)
                    if isinstance(result, dict) and "messages" in result and result["messages"]:
                        response_message = result["messages"][-1]
                        if hasattr(response_message, 'content') and response_message.content is not None:
                             st.markdown("**Researcher Result:**")
                             st.info(response_message.content)
                             output_messages.append(response_message)
                        else:
                            st.warning("Researcher returned message with no content.")
                            output_messages.append(AIMessage(content="[Researcher returned message with no content]", name="researcher_internal_error"))
                    elif isinstance(result, dict) and "output" in result: # Handle alternative agent output key
                        st.markdown("**Researcher Result (Output Key):**")
                        st.info(result["output"])
                        output_messages.append(AIMessage(content=result["output"], name="researcher")) # Assign name
                    else:
                        st.warning(f"Unexpected result format from Researcher: {type(result)}. Using placeholder.")
                        output_messages.append(AIMessage(content="[Researcher returned unexpected format]", name="researcher_internal_error"))

                    searched_this_cycle_update = True # Mark as ran

                except Exception as e:
                    st.error(f"Researcher execution failed: {e}")
                    st.json({"input_sent_to_researcher": agent_input}) # Log input on error
                    output_messages.append(AIMessage(content=f"Error during Researcher execution: {e}", name="researcher_execution_error"))
                    searched_this_cycle_update = True # Mark as attempted even on error

        # Always return to supervisor
        update_dict = {
            "messages": output_messages,
            # "querier_ran": current_querier_ran_status, # Preserve if needed
            "searched_this_cycle": searched_this_cycle_update # Mark run for this cycle
        }
        return Command(update=update_dict, goto="supervisor")

    # --- build_graph (No change needed in structure) ---
    def build_graph(self):
        # ... (graph definition remains the same: START -> load_memories -> supervisor -> workers -> supervisor -> END) ...
        builder = StateGraph(State)
        builder.add_node("load_memories", self.load_memories_node)
        builder.add_node("supervisor", self.supervisor_node)
        builder.add_node("researcher", self.research_node)
        builder.add_node("querier", self.query_node)
        builder.add_edge(START, "load_memories")
        builder.add_edge("load_memories", "supervisor")
        # Conditional edges and returns handle worker -> supervisor -> worker/END flow
        graph = builder.compile()
        print("Graph compiled successfully with memory loading and contextual task injection.")
        return graph


if __name__ == "__main__":
    import requests

    original_get = requests.get

    def custom_get(*args, **kwargs):
        # Set a higher default timeout
        kwargs.setdefault("timeout", 50)  # or even 60 if needed
        return original_get(*args, **kwargs)

    requests.get = custom_get

    graph = GraphBuilder(llm, querier).build_graph()
    graph_image = graph.get_graph(xray=True).draw_mermaid_png()
    # Save the graph image to a file and display it
    output_path = "/home/lumasty/Documents/GitHub/MAFinancialAssistance/SuperviseAgent/graph.png"
    with open(output_path, "wb") as f:
        f.write(graph_image)
    print(f"Graph image saved to {output_path}") 