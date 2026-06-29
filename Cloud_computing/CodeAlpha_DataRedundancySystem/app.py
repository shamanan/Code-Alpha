from flask import Flask, render_template, request
import sqlite3

app = Flask(__name__)

# Create database
def init_db():

    conn = sqlite3.connect('database.db')

    cursor = conn.cursor()

    cursor.execute('''
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        email TEXT UNIQUE
    )
    ''')

    conn.commit()
    conn.close()

init_db()

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/submit', methods=['POST'])
def submit():

    name = request.form['name']
    email = request.form['email']

    conn = sqlite3.connect('database.db')

    cursor = conn.cursor()

    # Check duplicate email
    cursor.execute(
        "SELECT * FROM users WHERE email=?",
        (email,)
    )

    existing_user = cursor.fetchone()

    if existing_user:

        message = "Duplicate Data Found! Entry Rejected."

    else:

        cursor.execute(
            "INSERT INTO users(name,email) VALUES(?,?)",
            (name, email)
        )

        conn.commit()

        message = "Unique Data Added Successfully."

    conn.close()

    return render_template(
        'index.html',
        message=message
    )

if __name__ == '__main__':
    app.run(debug=True)
    