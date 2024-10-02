<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Voter Login</title>
</head>
<body>
    <h1>Login</h1>

    <form action="VoterLoginServlet" method="POST">
        <label>Email:</label>
        <input type="email" name="email" required><br>
        
        <label>Password:</label>
        <input type="password" name="password" required><br>

        <button type="submit">Login</button>
    </form>
    <a href="voterRegister.jsp"><h4>Don't have an account ?</h4></a>
    <p style="color: red;">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </p>
</body>
</html>
