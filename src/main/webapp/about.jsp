<%@ page import="com.voting.models.Voter" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>About Us - Online Voting System</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f8f9fa;
            }
            header {
                background-color: #4CAF50;
                color: white;
                padding: 20px 0;
                text-align: center;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            nav {
                padding: 20px 0;
            }
            .welcome-section {
                text-align: center;
            }
            .nav-menu {
                list-style-type: none;
                padding: 0;
                display: flex;
                justify-content: center;
            }
            .nav-menu li {
                margin: 0 15px;
            }
            .nav-menu a {
                text-decoration: none;
                color: #4CAF50;
                padding: 10px 15px;
                border: 2px solid transparent;
                border-radius: 5px;
                transition: background-color 0.3s, color 0.3s, border-color 0.3s;
            }
            .nav-menu a:hover {
                background-color: #4CAF50;
                color: white;
                border-color: #4CAF50;
            }
            .btn-logout {
                background-color: #e74c3c;
                color: white;
            }
            .btn-logout:hover {
                background-color: #c0392b;
            }
            .about-container {
                margin-top: 100px;
                padding: 20px;
                text-align: center;
            }
            footer {
                margin-top: 50px;
                padding: 20px 0;
                background-color: #007bff;
                color: #fff;
                text-align: center;
            }
            footer a {
                color: #fff;
                text-decoration: none;
            }
            footer a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <header>
            <h1>About Our Online Voting System</h1>
        </header>

        <nav>
            <section class="welcome-section">
                <ul class="nav-menu">
                    <li><b>Want to know, <%= ((Voter) session.getAttribute("voter")).getName() %>?</b></li>
                    <li><a href="home.jsp">Live Votes</a></li>
                    <li><a href="vote.jsp">Vote Here</a></li>                 
                    <li><a href="contact.jsp">Contact Us</a></li>
                    <li><a href="about.jsp">About Us</a></li>
                    <li><a href="logout.jsp" class="btn-logout">Logout</a></li>
                </ul>
            </section>
        </nav>
        <div class="container about-container">
            <h1>About Us</h1>
            <p>Welcome to our Online Voting System!</p>
            <p>We are committed to providing a secure, anonymous, and transparent voting experience for all users. Our platform is designed to empower citizens and ensure that their voices are heard in a safe and efficient manner.</p>
            <p>Our mission is to revolutionize the voting process by leveraging technology to increase participation and enhance the integrity of elections.</p>
            <p>Thank you for choosing our system!</p>
        </div>

        <footer>
            <p>Copyright 2024 Your Company Name. All rights reserved.</p>
            <p>
                <a href="about.jsp">About Us</a> |
                <a href="contact.jsp">Contact Us</a> |
                <a href="privacy.jsp">Privacy Policy</a>
            </p>
        </footer>
    </body>
</html>
