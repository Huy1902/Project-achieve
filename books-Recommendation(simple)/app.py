from book_recommendation import get_recommends
from flask import Flask, request, jsonify, Response, send_from_directory
import os
import time


app = Flask(__name__)

# Path to the directory where images are saved
PATH_TO_TEST_IMAGES_DIR = os.path.join(os.path.dirname(__file__), 'images')

# Ensure directory exists
if not os.path.exists(PATH_TO_TEST_IMAGES_DIR):
    os.makedirs(PATH_TO_TEST_IMAGES_DIR)

IMAGE_LIST_FILE = os.path.join(PATH_TO_TEST_IMAGES_DIR, 'image_list.txt')


@app.route('/')
def index():
    return Response(open('./static/getImage.html').read(), mimetype="text/html")


# Save the image as a picture
@app.route('/save-image', methods=['POST'])
def save_image():
    i = request.files['image']  # get the image
    f = ('%s.jpeg' % time.strftime("%Y%m%d-%H%M%S"))
    i.save('%s/%s' % (PATH_TO_TEST_IMAGES_DIR, f))

    # Save the image name to a file
    with open(IMAGE_LIST_FILE, 'a') as file:
        file.write(f + '\n')

    return Response("%s saved" % f)


@app.route('/list-image', methods=['GET'])
def list_image():
    with open(IMAGE_LIST_FILE, 'r') as file:
        image_names = [line.strip() for line in file.readlines()]
    return {"images": image_names}


@app.route('/get-newest-image', methods=['GET'])
def get_image():
    with open(IMAGE_LIST_FILE, 'r') as file:
        image_names = [line.strip() for line in file.readlines()]
    if image_names:
        return send_from_directory(PATH_TO_TEST_IMAGES_DIR, image_names[-1])
    return Response("No images found", status=404)


@app.route('/get-recommend', methods=['GET'])
def get_recommend():
    title = request.args.get('title')
    book_recommendations = get_recommends(title)
    return jsonify(list(book_recommendations))


@app.route('/images/<filename>')
def send_image(filename):
    return send_from_directory(PATH_TO_TEST_IMAGES_DIR, filename)


if __name__ == '__main__':
    app.run()
