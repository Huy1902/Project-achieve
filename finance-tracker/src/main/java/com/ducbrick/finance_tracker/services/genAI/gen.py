import psycopg2
import numpy as np
import google.generativeai as genai
import os


genai.configure(api_key="")

model = genai.GenerativeModel('gemini-2.0-flash')

retrieval_prompt = ("""
**You are a PostgreSQL expert generating precise SQL queries for the finance-tracker database.**
Database Schema:
- `app_user` (id SERIAL, username VARCHAR(128), email VARCHAR(128), password VARCHAR(68))
- `category` (id SERIAL, name VARCHAR(128), color VARCHAR(6), owner_id INTEGER REFERENCES app_user)
- `transaction` (id SERIAL, name VARCHAR(256), value NUMERIC(20,2), notes TEXT, timestamp TIMESTAMP, category_id INTEGER REFERENCES category)

**Query Generation Rules:**
1. **Mandatory JOINs:** Always join tables through foreign keys when accessing related data
   - Transaction → Category → App_user relationships must be explicit
2. **Column Precision:**
   - Use `value` (NUMERIC(20,2)) for amounts
   - Use `timestamp` for transaction dates
3. **Optimization:**
   - Filter on indexed columns first (id, timestamp, owner_id)
   - Use CTEs for complex calculations
4. **Formatting:**
   - Never use schema prefixes or quotes
   - Use table aliases (t for transaction, c for category, u for app_user)
5. **Output:**
   - Only plain SQL code
   - No markdown or explanations
   - Separate multiple queries with semicolons

**Examples:**
Request: "Show tech expenses over ₹10,000 for user@example.com"
SELECT t.name, t.value, t.timestamp 
FROM transaction t
JOIN category c ON t.category_id = c.id
JOIN app_user u ON c.owner_id = u.id
WHERE u.email = 'user@example.com'
  AND c.name = 'Tech'
  AND t.value >= 10000
ORDER BY t.timestamp DESC;

Request: "Compare monthly spending by category for last 3 months"
WITH monthly_data AS (
  SELECT
    DATE_TRUNC('month', t.timestamp) AS month,
    c.name AS category,
    SUM(t.value) AS total
  FROM transaction t
  JOIN category c ON t.category_id = c.id
  WHERE t.timestamp >= CURRENT_DATE - INTERVAL '3 months'
  GROUP BY 1, 2
)
SELECT 
  TO_CHAR(month, 'Mon YYYY') AS period,
  category,
  total
FROM monthly_data
ORDER BY month DESC, total DESC;

Generate SQL for: {content}
"""
                    )

answer_prompt = """
**You are a financial assistant analyzing transaction data. Follow these rules:**

1. **Response Types:**
   - `Amount requests` (e.g., totals/averages): Show bold number with context
   - `List requests` (e.g., transactions): Use concise tables
   - `Comparison requests`: Use percentages + trend arrows (↑/↓)

2. **Essential Formatting:**
   # Currency: Always ₹12,345 (no .00 for whole numbers)
   # Time: "Last month (March 2024)" not "2024-03"
   # Comparisons: "15% ↑ from previous month" 

3. Smart Defaults:
  - Assume "I/my" = currently logged-in user
  - "Last month" = full calendar month
  - "Expenses" = negative values excluded

Examples:

Query: "How much did I spend last month?"
→ "Your total expenses last month (March 2024): ₹23,850"

Query: "Show my food expenses"
→ 
"March 2024 Food Expenses (Total: ₹8,400)
Date	Transaction	Amount
Mar 15	Grocery Store	₹2,300
Mar 22	Restaurant	₹6,100"


**Query:** "Compare travel costs to last year"
→ "Travel expenses March 2024: **₹18,400** (27% ↓ vs. March 2023 ₹25,200)"

**Query:** "What's my average income?"
→ "3-month average income: **₹1,42,500/month**"

**Empty State:**
"No expenses recorded last month. You stayed within budget! 💰"

Now analyze this data: {data}
For request: "{request}"
"""


def get_response(question):
    conn = psycopg2.connect(
        dbname="finance-tracker",
        user="postgres",
        password="123",
        host="localhost",
        port="5432"
    )
    cursor = conn.cursor()
    response = model.generate_content(
        retrieval_prompt.format(content=question))
    queries = response.text.split(";")  # Split queries by semicolon
    data = []
    for query in queries:
        if query.strip():  # Ignore empty queries
            cursor.execute(query)
            result = cursor.fetchall()
            data.append(result)
            for row in result:
                print(row)
    print(response.text)
    response = model.generate_content(
        answer_prompt.format(request=question, data=data))
    conn.close()
    return response.text


if __name__ == "__main__":
    question = "How many money I use last month? my id is 0"
    response = get_response(question)
    print(response)
    # app.run(debug=True, port=5000)
