import uuid
import os
import psycopg2
from datetime import datetime
import streamlit as st
from sqlalchemy import create_engine
from langchain_community.utilities import SQLDatabase
from DBAgent.query_prompt import get_query_prompt
from DBAgent.tool import define_tool
from DBAgent.graph import build_graph_from


# --- Define a function that creates the psycopg2 connection ---
def create_pg_connection():
    try:
        db_password = os.environ.get("DB_PASSWORD") # Get password securely
        cnx = psycopg2.connect(user="huy", password=db_password, host="mafinance.postgres.database.azure.com", port=5432, database="postgres")
        return cnx
    except Exception as e:
        print(f"Error creating psycopg2 connection: {e}")
        raise # Re-raise the exception

class Querier():
    def __init__(self, llm):
        self.init_database()
        tools = define_tool(self.db, llm)
        chain = get_query_prompt() | llm.bind_tools(tools)
        self.graph = build_graph_from(chain, tools)
    
    def init_database(self):
        engine = create_engine(
            "postgresql+psycopg2://", # Dialect + Driver
            creator=create_pg_connection,
        )
        self.db = SQLDatabase(engine=engine)

    def execute_query(self, query):
        # Logic to execute a database query
        
        # Use this unique ID to keep track of the session
        _printed = set()
        thread_id = str(uuid.uuid4())
        config = {
            "configurable": {
                # Checkpoints are accessed by thread_id
                "thread_id": thread_id,
            }
        }
        # events = self.graph.stream(
        #     {"messages": [("user", query)]}, config, stream_mode="values"
        # )
        # for event in events:
        #     _print_event(event, _printed)
        current_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        query = query + f" My account ID is:{st.session_state.user_id}. Today is {current_time} (Vietnam Time)"
        msg = {"messages": [("user", query)]}
        print("Query: " + query)
        messages = self.graph.invoke(msg,config)
        # print(messages['messages'][-1].content)
        return messages


# if __name__ == "__main__":
#     agent = Querier()
#     agent.execute_query("What is the average amount of money I consume per month? My account ID is 0")