<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Election</title>
    </head>
    <body>
        <h1>Create a New Election</h1>

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

                // Validate date range
                if (!validateDates(startDate, endDate)) {
                    alert("Start date must be before or equal to the end date.");
                    return false; // Prevent form submission
                }            
               return true;
            }
        </script>
    </body>
</html>