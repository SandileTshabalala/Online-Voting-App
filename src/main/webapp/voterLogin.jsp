<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Voter Login</title>
            <style>
           body {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            background: red; /* Background color before wave */
            background-image:url("https://cdn.ghanaguardian.com/2024/07/AI-elections_illo_SamanthaWongAdobeStock-750x375-1-610x375.webp");
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #30C3C6; /* Main background color */
            overflow: hidden; /* Prevent scrolling */
            margin: 0;
            height: 100vh; /* Set body height to viewport height */
            display: flex; /* Use flexbox for centering */
            justify-content: center; /* Center horizontally */
            align-items: center; /* Center vertically */
            position: relative; /* Positioning for the wave */
        }

        .container {
            background-color: #fff; /* Set a white background for contrast */
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0px 10px 30px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
            text-align: center;
            z-index: 1; /* Ensure container is above background */
        }

        h1 {
            font-size: 2.5em;
            color: #333;
            margin-bottom: 20px;
        }

        form {
            width: 100%;
        }

        label {
            display: block;
            font-size: 0.9em;
            margin-bottom: 8px;
            color: #555;
            text-align: left;
        }

        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 1em;
            background-color: #f9f9f9;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }

        input[type="email"]:focus,
        input[type="password"]:focus {
            border-color: #007bff;
            outline: none;
        }

        button {
            width: 100%;
            padding: 12px;
            background-color: #28a745;
            color: #fff;
            border: none;
            border-radius: 8px;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s ease, box-shadow 0.3s ease;
        }

        button:hover {
            background-color: #218838;
            box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
        }

        .links {
            margin-top: 20px;
            font-size: 0.9em;
        }

        .links a {
            color: #007bff;
            text-decoration: none;
            margin: 5px 0;
            display: block;
        }

        .links a:hover {
            color: #0056b3;
            text-decoration: underline;
        }

        p {
            color: red;
            font-size: 0.9em;
            margin-top: 15px;
        }

        /* Dot Wave Background */
        body::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><path fill="%23ffffff" d="M0 50c10 0 10-10 20-10s10 10 20 10 10-10 20-10 10 10 20 10 10-10 20-10v100H0z" /></svg>') repeat; /* Change fill to white */
            z-index: 0; /* Ensure background is behind content */
            opacity: 0.1; /* Set opacity for a subtle effect */
        }

    </style>
</head>
<body>
    <div class="dot-wave"></div> <!-- Dot wave background -->
    <div class="container">
        <h1 style="color: #28A745">Login</h1>

        <form action="VoterLoginServlet" method="POST">
            <label for="email">Email:</label>
            <input type="email" name="email" id="email" required>

            <label for="password">Password:</label>
            <input type="password" name="password" id="password" required>

            <button type="submit">Login</button>
        </form>

        <div class="links">
            <a href="forgotPassword.jsp">Forgot Password?</a>
          <p style="color:black">Don't have an account? <a href="voterRegister.jsp"> Sign Up</a></p>
        </div>
        

        <p>
            <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
        </p>
    </div>
</body>
</html>
