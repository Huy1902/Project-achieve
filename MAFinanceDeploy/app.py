# UI by streamlit
import os
import streamlit as st
from dotenv import load_dotenv
from SuperviseAgent.Supervisor import Supervisor
from langchain_google_genai import ChatGoogleGenerativeAI
import requests

load_dotenv(override=True)

st.set_page_config(page_title="Finance Assistant", page_icon="😇")
st.title("Finace Assistant")

# Get Gateway URL from environment variable or set default for local testing
GATEWAY_BASE_URL = os.getenv("GATEWAY_URL") # Default to local gateway

# --- Get User ID from Gateway Function ---
def get_user_id_from_gateway():
    """
    Retrieves the User ID by calling the Flask gateway.
    Expects the chat_token to be passed as a URL query parameter named 'token'.
    """
    # Use st.query_params which is the modern way to get parameters
    token = st.query_params.get("token")
    
    print(token) # Debug print
    
    
    if not token:
        st.error("No session token found in URL. Please access this app via the link provided by the main application.")
        st.stop() # Stop execution if no token
        return None # Should not be reached due to st.stop()

    user_url = f"{GATEWAY_BASE_URL}/gateway/session/user"
    params = {'token': token}
    # IMPORTANT: If you re-enable API keys on the gateway, add headers here:
    # headers = {'X-API-Key': 'YOUR_CHATBOT_API_KEY'} # Get key securely
    # response = requests.get(user_url, params=params, headers=headers, timeout=10)
    try:
        print(f"Attempting to get user ID from gateway: GET {user_url} with token {token}")
        response = requests.get(user_url, params=params, timeout=10)
        response.raise_for_status() # Raise HTTPError for bad responses (4xx or 5xx)

        user_data = response.json()
        if 'user_id' in user_data:
            print(f"Successfully retrieved user ID: {user_data['user_id']}")
            return user_data['user_id']
        else:
            st.error("Gateway did not return a valid User ID.")
            st.stop() # Stop if gateway response is invalid
            return None

    except requests.exceptions.HTTPError as http_err:
        # Check response before accessing response.status_code or response.text
        status_code = http_err.response.status_code if http_err.response is not None else None
        response_text = http_err.response.text if http_err.response is not None else "No response body"

        if status_code == 404:
             st.error("Invalid or expired session token. Please restart the chat from the main application.")
        elif status_code == 401:
             st.error("Unauthorized access to gateway. Check API Key configuration.") # If keys are enabled
        else:
             st.error(f"HTTP error occurred contacting gateway: {http_err} - Status: {status_code} - Response: {response_text}")
        st.stop()
        return None
    except requests.exceptions.ConnectionError as conn_err:
        st.error(f"Could not connect to the gateway at {GATEWAY_BASE_URL}. Please ensure it's running and accessible. Error: {conn_err}")
        st.stop()
        return None
    except requests.exceptions.Timeout:
        st.error(f"Request to the gateway timed out.")
        st.stop()
        return None
    except Exception as e: # Catch any other errors
        st.error(f"An unexpected error occurred while fetching User ID: {e}")
        st.stop()
        return None

# --- Initialize User ID in Session State ---
# This replaces the static get_account_id() call from your original code
st.session_state.user_id = get_user_id_from_gateway()

# --- Load other environment variables (as in your original code) ---
base_model = os.getenv("MODEL")
base_api = os.getenv("BASE_API")


with st.sidebar:
    model = "Gemini-2-flash"
    st.info("Model: " + model)
    st.info(f"Current User ID: {st.session_state.user_id}")
return_message = False
# print(model)
llm = ChatGoogleGenerativeAI(
model='gemini-2.0-flash', google_api_key=base_api, temperature=0)

agent = Supervisor(llm)

if "messages" not in st.session_state:
    st.session_state["messages"] = [
        {"role": "assistant",
            "content": "Hi, I'm a chatbot who can search the web. How can I help you?"}
    ]

st.session_state.short_memory = ""
for msg in st.session_state.messages:
    st.chat_message(msg["role"]).write(msg["content"])
    st.session_state.short_memory += f"{msg['role']}: {msg['content']}\n"
if usr_msg := st.chat_input():
    st.session_state.messages.append(
        {"role": "user", "content": usr_msg})
    st.chat_message("user").write(usr_msg)

    with st.chat_message("assistant"):
        try:
            st.write(usr_msg)
            message = agent.execute(usr_msg)
            if message:
                st.session_state.messages.append(
                    {"role": "assistant", "content": message})
            if message:
                st.markdown(message)
            else:
                # This path indicates an issue
                st.error("Agent finished unexpectedly or could not determine a final response.")
        except Exception as e:
            st.write(f"Error: {e}")
