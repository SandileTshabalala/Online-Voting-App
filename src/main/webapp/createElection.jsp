<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Election</title>
        <style>
            h1 {
                color: #18181B; 
                text-align: center;
            }
            form {
                background-color: #fff; 
                padding: 20px; 
                border-radius: 8px; 
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); 
                margin-bottom: 20px; 
            }
            input[type="text"],
            input[type="date"],
            textarea,
            select {
                width: 100%; 
                padding: 10px; 
                margin: 10px 0; 
                border: 1px solid #ccc; 
                border-radius: 4px; 
            }
            button {
                background-color: #18181B; 
                color: #fff; 
                padding: 10px 15px; 
                border: none; 
                border-radius: 4px;
                cursor: pointer; 
            }

            button:hover {
                background-color: #333;
            }
            a {
                color: #007BFF;
                text-decoration: none; 
            }

            a:hover {
                text-decoration: underline; 
            }
        </style>
    </head>
    <body>
        <h1>Create a New Election</h1><br>

        <form id="electionForm" action="ElectionServlet" method="post" onsubmit="return submitForm();">
            <input type="hidden" name="action" value="create">

            <label for="name">Election Name:</label>
            <input type="text" id="name" name="name" required><br><br>

            <label for="description">Description:</label>
            <textarea id="description" name="description" required></textarea><br><br>

            <label for="startDate">Start Date:</label>
            <input type="date" id="startDate" name="startDate" required><br><br>

            <label for="endDate">End Date:</label>
            <input type="date" id="endDate" name="endDate" required><br><br>

            <label for="status">Status:</label>
            <select id="status" name="status" required>
                <option value="Active">Active</option>
                <option value="Inactive">Inactive</option>
            </select><br><br>

            <label for="positions">Available Positions (comma separated):</label>
            <input type="text" id="positions" name="positions" required placeholder="e.g., President, Treasurer, Secretary"><br><br>

            <button type="submit">Create Election</button>
        </form>

        <br>
        <a href="elections.jsp">Elections Created</a>

        <script>
            console.log("Script block executed");
            function validateDates(startDate, endDate) {
                return new Date(startDate) <= new Date(endDate);
            }

            function submitForm() {
                console.log("submitForm called");
                const startDate = document.getElementById("startDate").value;
                const endDate = document.getElementById("endDate").value;

                // Validate date 
                if (!validateDates(startDate, endDate)) {
                    alert("Start date must be before or equal to the end date.");
                    return false;
                }
                return true;
            }
        </script>
    </body>
</html>