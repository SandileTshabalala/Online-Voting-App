<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Admin Dashboard</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                margin: 0;
                padding: 0;
            }
            .container {
                display: flex;
                height: 100vh;
            }
            .sidebar {
                width: 250px;
                background-color: #007bff;
                padding: 20px;
                color: white;
                display: flex;
                flex-direction: column;
                align-items: flex-start;
                transition: width 0.3s;
            }
            .sidebar.hide {
                width: 0;
                padding: 0;
                overflow: hidden;
            }
            .sidebar a {
                color: white;
                text-decoration: none;
                font-weight: bold;
                margin: 15px 0;
                cursor: pointer;
                opacity: 1;
                transition: opacity 0.3s;
            }
            .sidebar.hide a {
                opacity: 0;
            }
            .sidebar a:hover {
                text-decoration: underline;
            }
            .main-content {
                flex-grow: 1;
                padding: 20px;
                background-color: #f4f4f4;
                transition: margin-left 0.3s;
            }
            .sidebar-title {
                margin-bottom: 30px;
                font-size: 24px;
                font-weight: bold;
            }
            .toggle-btn {
                position: absolute;
                top: 10px;
                left: 260px;
                background-color: #007bff;
                color: white;
                border: none;
                padding: 10px 15px;
                cursor: pointer;
                transition: left 0.3s;
            }
            .sidebar.hide + .toggle-btn {
                left: 10px;
            }
            .sidebar.hide + .toggle-btn::after {
                content: 'Show Sidebar';
            }
            .toggle-btn::after {
                content: 'Hide Sidebar';
            }
        </style>
        <script>
            function toggleSidebar() {
                var sidebar = document.querySelector('.sidebar');
                var mainContent = document.querySelector('.main-content');
                var toggleBtn = document.querySelector('.toggle-btn');
                sidebar.classList.toggle('hide');
                if (sidebar.classList.contains('hide')) {
                    mainContent.style.marginLeft = '0';
                } else {
                    mainContent.style.marginLeft = '250px';
                }
            }


            function loadContent(page) {
                // Create an AJAX request to load the content dynamically
                fetch(page)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok.');
                            }
                            return response.text();
                        })
                        .then(html => {
                            document.getElementById("main-content").innerHTML = html;
                        })
                        .catch(error => {
                            console.error('There has been a problem with your fetch operation:', error);
                        });
            }

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
                // If validation passes, the form is submitted
                return true;
            }

            // Load a default page when the dashboard first opens
            window.onload = function () {
                loadContent('dashboard.jsp');
            };
        </script>
    </head>
    <body>
        <div class="container">
            <!-- Sidebar -->
            <div class="sidebar">
                <div class="sidebar-title">Admin Dashboard</div>
                <a onclick="loadContent('dashboard.jsp')">Dashboard</a>
                <a onclick="loadContent('monitorVoting.jsp')">Monitor Voting</a>
                <a onclick="loadContent('viewStatistics.jsp')">View Statistics</a>
                <a onclick="loadContent('createElection.jsp')">Create Election</a>
                <a onclick="loadContent('manageVoters.jsp')">Manage Voters</a>               
                <a onclick="loadContent('manageCandidates.jsp')">Manage Candidates</a>
                <a onclick="loadContent('generateReports.jsp')">Generate Reports</a>
                <a onclick="loadContent('settings.jsp')">Settings</a>
            </div>

            <!-- Toggle Button -->
            <button class="toggle-btn" onclick="toggleSidebar()"></button>

            <div id="main-content" class="main-content">
            </div>
        </div>
    </body>
</html>
