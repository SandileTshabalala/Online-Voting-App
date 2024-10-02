<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Voter Registration</title>
</head>
<body>
    <h1>Register as a Voter</h1>

    <form action="VoterRegisterServlet" method="POST">
        <label>Name:</label>
        <input type="text" name="name" required><br>
        
        <label>SurName:</label>
        <input type="text" name="surName" required><br>
        
        <label>Email:</label>
        <input type="email" name="email" required><br>
        
        <label>Student Number:</label>
        <input type="text" name="studentNumber" required><br>
        
        <label>Phone Number:</label>
        <input type="tel" name="phoneNumber" required><br>
        
        <label>Password:</label>
        <input type="password" name="password" required><br>
        
        <label>Confirm Password:</label>
        <input type="password" name="confirmPassword" required><br>

        <button type="submit">Register</button>
    </form>
     <a href="voterLogin.jsp"><h4>Already have an account ?</h4></a>
    <p style="color: red;">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </p>
</body>
</html>
