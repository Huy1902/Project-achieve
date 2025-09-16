import os
import secrets  # For generating secure tokens
import redis
from flask import Flask, request, jsonify
from datetime import timedelta

# --- Configuration ---
app = Flask(__name__)

TOKEN_EXPIRY_SECONDS = 300 # 5 minutes
REDIS_HOST = "redis-16516.c73.us-east-1-2.ec2.redns.redis-cloud.com"
REDIS_PORT = 16516
REDIS_USERNAME = "default"
REDIS_PASSWORD = "2KXOizLra0TUvXB0YWryupcRyf7bJig8"

redis_client = None # Initialize client to None
try:
    print(f"Attempting to connect to Redis at {REDIS_HOST}:{REDIS_PORT} with user '{REDIS_USERNAME}'...")
    # Use redis.Redis with hardcoded parameters
    redis_client = redis.Redis(
        host=REDIS_HOST,
        port=int(REDIS_PORT), # Port needs to be an integer
        username=REDIS_USERNAME,
        password=REDIS_PASSWORD,
        decode_responses=True # Makes Redis return strings instead of bytes
        # Add other options like ssl=True if needed for your Redis provider
        # ssl=True, # Example if SSL is needed
        # ssl_cert_reqs='required' # etc. - Check your provider's requirements
    )
    redis_client.ping() # Test connection
    print(f"Successfully connected to Redis at {REDIS_HOST}:{REDIS_PORT}")
except redis.exceptions.AuthenticationError as e:
    print(f"FATAL ERROR: Redis authentication failed. Check hardcoded credentials.")
    print(f"Error details: {e}")
    redis_client = None # Indicate connection failure
except redis.exceptions.ConnectionError as e:
    print(f"FATAL ERROR: Could not connect to Redis at {REDIS_HOST}:{REDIS_PORT}. Check hardcoded host/port and Redis server status/firewall.")
    print(f"Error details: {e}")
    redis_client = None # Indicate connection failure
except ValueError as e:
    print(f"FATAL ERROR: Invalid Redis port number '{REDIS_PORT}'. Please check the hardcoded REDIS_PORT value.")
    print(f"Error details: {e}")
    redis_client = None
except Exception as e: # Catch any other potential errors during connection
    print(f"FATAL ERROR: An unexpected error occurred during Redis connection.")
    print(f"Error details: {e}")
    redis_client = None


# --- Helper Functions ---

def generate_token():
    """Generates a secure, URL-safe random token."""
    return secrets.token_urlsafe(32)

def store_token(token, user_id):
    """Stores the token -> user_id mapping in Redis with expiry."""
    if not redis_client:
        print("Error: Cannot store token, Redis client is not connected.")
        return False
    try:
        redis_key = f"chat_token:{token}"
        redis_client.setex(redis_key, timedelta(seconds=TOKEN_EXPIRY_SECONDS), user_id)
        print(f"Stored token '{redis_key}' for user_id: {user_id} (expires in {TOKEN_EXPIRY_SECONDS}s)")
        return True
    except redis.exceptions.RedisError as e:
        print(f"Error storing token '{redis_key}' in Redis: {e}")
        return False

def get_user_from_token(token):
    """Retrieves the user_id associated with a token from Redis."""
    if not redis_client:
        print("Error: Cannot retrieve token, Redis client is not connected.")
        return None
    try:
        redis_key = f"chat_token:{token}"
        user_id = redis_client.get(redis_key)
        if user_id:
             print(f"Retrieved user_id '{user_id}' for token '{redis_key}'")
        else:
             print(f"Token '{redis_key}' not found or expired in Redis.")
        return user_id
    except redis.exceptions.RedisError as e:
        print(f"Error retrieving token '{redis_key}' from Redis: {e}")
        return None

# API Key decorator function is removed as it's no longer used

# --- API Endpoints ---

@app.route('/')
def home():
    """Simple home route for basic check."""
    if not redis_client:
        return "Gateway is running, but Redis connection FAILED.", 503
    return "Gateway is running and Redis connection appears OK."


# API Key decorator removed
@app.route('/gateway/session/start', methods=['POST'])
def start_chat_session():
    """
    Endpoint for the Java App to call.
    Receives: JSON body {"user_id": "some_user_id"}
    Returns: JSON {"chat_token": "generated_token", "expires_in": seconds} or error
    """
    if not redis_client:
         print("Error in /gateway/session/start: Redis not available.")
         return jsonify({"error": "Gateway internal error: Service temporarily unavailable"}), 503

    if not request.is_json:
        return jsonify({"error": "Request must be JSON"}), 400

    data = request.get_json()
    user_id = data.get('user_id')

    if not user_id or not isinstance(user_id, str) or not user_id.strip():
        return jsonify({"error": "Missing or invalid 'user_id' in request body"}), 400

    token = generate_token()
    if store_token(token, user_id):
        # print(f"Generated token {token} for user_id {user_id}") # Already printed in store_token
        return jsonify({"chat_token": token, "expires_in": TOKEN_EXPIRY_SECONDS}), 201 # 201 Created
    else:
        # Error already printed in store_token
        return jsonify({"error": "Failed to store session token"}), 500 # Internal Server Error


# API Key decorator removed
@app.route('/gateway/session/user', methods=['GET'])
def get_user_for_chatbot():
    """
    Endpoint for the Chat Bot to call.
    Receives: Query parameter ?token=chat_token_value
    Returns: JSON {"user_id": "retrieved_user_id"} or error
    """
    if not redis_client:
         print("Error in /gateway/session/user: Redis not available.")
         return jsonify({"error": "Gateway internal error: Service temporarily unavailable"}), 503

    token = request.args.get('token')
    if not token:
        return jsonify({"error": "Missing 'token' query parameter"}), 400

    user_id = get_user_from_token(token)

    if user_id:
        # print(f"Validated token {token}, returning user_id: {user_id}") # Already printed in get_user_from_token
        return jsonify({"user_id": user_id}), 200
    else:
        # print(f"Invalid or expired token received: {token}") # Already printed in get_user_from_token
        return jsonify({"error": "Invalid or expired token"}), 404


# --- Run the App (for local development) ---
# Render will use Gunicorn via the Procfile
if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5001))
    print(f"Starting Flask app on host 0.0.0.0 port {port}")
    if redis_client is None:
        print("FATAL: Cannot start Flask app because Redis connection failed during initialization.")
    else:
        # Turn debug OFF for production/Render
        app.run(host='0.0.0.0', port=port, debug=False)