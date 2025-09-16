from DBAgent.Querier import Querier
import os
import sys
import uuid
import streamlit as st


from SuperviseAgent.GraphBuilder import GraphBuilder
from langchain_core.messages import HumanMessage, AIMessage, SystemMessage
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_core.prompts import ChatPromptTemplate

sys.path.append(os.path.abspath(
    "/home/lumasty/Documents/GitHub/MAFinancialAssistance"))


class Supervisor():
    def __init__(self, llm):
        base_model = os.getenv("MODEL")
        base_api = os.getenv("BASE_API2")
        llm2 = ChatGoogleGenerativeAI(
        model=base_model, google_api_key=base_api, temperature=0)
        querier = Querier(llm2)
        self.graph = GraphBuilder(llm, querier).build_graph()
        self.llm3 = ChatGoogleGenerativeAI(
            model="gemini-2.0-flash", google_api_key=base_api, temperature=0
        )
        self.prompt_template = ChatPromptTemplate.from_messages(
            [
                SystemMessage(
                    content="You are an assistant skilled in concisely summarizing answers, focusing on addressing the original question asked."
                ),
                HumanMessage(
                    content="Original Question:\n```\n{question}\n```\n\n"
                    "Context:\n```\n{conversation}\n```\n\n"
                    "Generated Answer:\n```\n{answer}\n```\n\n"
                    "Based on the original question, please provide a concise summary of the generated answer above. Focus only on the key points that directly address the question."
                    "Remember to adapt with social questions and provide a friendly tone."
                ),
            ]
        )
        self.chain = self.prompt_template | self.llm3

    def execute(self, prompt):
        if "thread_id" not in st.session_state:
            st.session_state.thread_id = str(uuid.uuid4()) # Persist thread_id across reruns
        initial_graph_state = {
            "messages": [HumanMessage(content=prompt)],
                # Reset cycle flags for a new user input
            "queried_this_cycle": False,
            "searched_this_cycle": False,
            "next": "", # Initialize next state
            "recall_memories": None 
        }
        

        st.markdown("---")
        st.subheader("Agent Execution Log:") # Changed title
        log_container = st.container(height=300) # Container for logs

        # Keep track of the last relevant message content from a worker
        last_worker_message_content = None
        supervisor_signaled_finish = False
        final_state_messages = [] # Store full message history from final state

        config = {"configurable": {"thread_id": st.session_state.thread_id}} # Use persistent thread_id

        # Stream graph execution
        for step in self.graph.stream(initial_graph_state, config=config):
            node_name, state_update = next(iter(step.items()))
            with log_container: # Write logs inside the container
                st.write(f"Executing Node: **{node_name}**") # Log node name

            # Store the full list of messages from the latest state update
            if isinstance(state_update, dict) and "messages" in state_update:
                    final_state_messages = state_update["messages"] # Keep track of the latest message list

            # Capture the latest message content added by worker nodes in this step
            if node_name in ["querier", "researcher"]:
                    if isinstance(state_update, dict) and "messages" in state_update and state_update["messages"]:
                        newest_message = state_update["messages"][-1]
                        if hasattr(newest_message, 'content') and newest_message.content is not None:
                            is_error = False
                            if hasattr(newest_message, 'name') and isinstance(newest_message.name, str):
                                if 'error' in newest_message.name:
                                    is_error = True
                            if not is_error:
                                last_worker_message_content = newest_message.content

            # Check if the supervisor decided to FINISH in this step
            if node_name == "supervisor":
                    if isinstance(state_update, dict) and state_update.get("next") == "FINISH":
                        with log_container:
                            st.success("Supervisor decided FINISH.")
                        supervisor_signaled_finish = True
                        # Don't break here, let the stream end naturally

        # --- Final Response Handling ---
        st.markdown("---") # Separator after execution log
        final_response_content = None
        if last_worker_message_content:
            final_response_content = last_worker_message_content
        elif supervisor_signaled_finish:
            # If supervisor finished but no relevant worker message was captured
            # Maybe use the last AI message from the history if available?
            if final_state_messages and isinstance(final_state_messages[-1], AIMessage):
                    final_response_content = final_state_messages[-1].content
            else:
                final_response_content = "Okay, the task is complete." # Default confirmation
                
        response = self.chain.invoke({"conversation": st.session_state.short_memory, "question": prompt, "answer": final_response_content})
        summary = response.content if hasattr(response, 'content') else str(response)
        return summary
