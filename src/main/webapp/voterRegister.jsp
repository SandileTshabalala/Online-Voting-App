<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Voter Registration</title>
    <style>
         body {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            background: red; 
            background-image:url("https://cdn.ghanaguardian.com/2024/07/AI-elections_illo_SamanthaWongAdobeStock-750x375-1-610x375.webp");

        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #30C3C6; 
            overflow: hidden; 
            margin: 0;
            height: 100vh;
            display: flex; 
            justify-content: center; 
            align-items: center; 
            position: relative;
        }

        .container {
            background-color: #fff; 
            padding: 15px; 
            border-radius: 10px; 
            box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.1); 
            max-width: 350px; 
            width: 100%;
           margin: 20px 30px;
            text-align: center;
            z-index: 1;
        }

        h1 {
            font-size: 2em; 
            color: #333;
            margin-bottom: 15px; 
        }

        label {
            display: block;
            font-size: 0.9em;
            margin-bottom: 8px;
            color: #555;
            text-align: left;
        }

        .input-wrapper {
            display: flex; 
            justify-content: space-between; 
            margin-bottom: 15px; 
        }

        input[type="text"],
        input[type="email"],
        input[type="tel"],
        input[type="password"] {
            flex: 1;
            padding: 10px; 
            margin-left: 10px; 
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 0.9em; 
            background-color: #f9f9f9;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="tel"]:focus,
        input[type="password"]:focus {
            border-color: #007bff;
            outline: none;
        }

        button {
            width: 100%;
            padding: 10px; 
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
            box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.1); 
        }

        .links {
            margin-top: 15px;
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
            color: black;
            font-size: 0.9em;
            margin-top: 10px; 
        }

        body::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><path fill="%23ffffff" d="M0 50c10 0 10-10 20-10s10 10 20 10 10-10 20-10 10 10 20 10 10-10 20-10v100H0z" /></svg>') repeat; /* Change fill to white */
            z-index: 0; 
            opacity: 0.1; 
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Voter Registration</h1><hr><br>

        <form action="VoterRegisterServlet" method="POST">
            <div class="input-wrapper">
               
                <input type="text" name="name" placeholder="Enter your name" required>
            </div>
            
            <div class="input-wrapper">
          
                <input type="text" name="surName" placeholder="Enter your surname" required>
            </div>
            
            <div class="input-wrapper">
          
                <input type="email" name="email" placeholder="Enter your email" required>
            </div>
            
            <div class="input-wrapper">
                
                <input type="text" name="studentNumber" placeholder="Enter your student number" required>
            </div>
            
            <div class="input-wrapper">
             
                <input type="tel" name="phoneNumber" placeholder="Enter your phone number" required>
            </div>
            
            <div class="input-wrapper">
                
                <input type="password" name="password" placeholder="Enter your password" required>
            </div>
            
            <div class="input-wrapper">
             
                <input type="password" name="confirmPassword" placeholder="Confirm your password" required>
            </div>

            <button type="submit">Register</button>
        </form>
    <div class="links">
                 <p>Already have an account?  <a href="voterLogin.jsp">Login</a></p>
         <div class="links">
           <p>Run as Candidate?<a href="candidateRegister.jsp">register</a></p> 
        </div>
        <p style="color: red;">
            <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
        </p>
    </div>
</body>
</html>
