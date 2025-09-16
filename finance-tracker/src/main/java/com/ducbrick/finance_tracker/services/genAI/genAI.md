# Guide to Create a Virtual Environment and Install Requirements

## Step 1: Create a Virtual Environment

1. Open your terminal.
2. Navigate to your project directory:
  ```sh
  cd /home/lumasty/Documents/GitHub/finance-tracker/src/main/java/com/ducbrick/finance_tracker/services/genAI
  ```
3. Create a virtual environment:
  ```sh
  python3 -m venv venv
  ```

## Step 2: Activate the Virtual Environment

- On macOS and Linux:
  ```sh
  source venv/bin/activate
  ```
- On Windows:
  ```sh
  .\venv\Scripts\activate
  ```

## Step 3: Install Requirements

1. Ensure you have a `requirements.txt` file in your project directory.
2. Install the dependencies:
  ```sh
  pip install -r requirements.txt
  ```

## Step 4: Verify Installation

1. Check the installed packages:
  ```sh
  pip list
  ```

## Deactivating the Virtual Environment

- To deactivate the virtual environment, simply run:
  ```sh
  deactivate
  ```

That's it! You have successfully set up a virtual environment and installed the required packages.


***Note***: You need to run it before use chatbot in project