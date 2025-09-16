from flask import Flask, request, jsonify
from gen import get_response

app = Flask(__name__)

@app.route('/get-response', methods=['GET'])
def get_response():
  question = request.args.get('question')
  response = get_response(question)
  return jsonify({'response': response}) 


if __name__ == '__main__':
  app.run(debug=True, port=5000)