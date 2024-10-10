<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
  
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            min-height: 100vh;
        }

        .sidebar {
            width: 250px;
            background-color: #18181B;
            color: white;
            padding: 20px;
            position: fixed;
            height: 100%;
            overflow-y: auto;
            transition: width 0.3s;
            z-index: 1000;
        }

        .sidebar.hide {
            width: 0;
            padding: 20px 0;
        }

        .sidebar a {
            display: flex;
            align-items: center;
            color: white;
            text-decoration: none;
            font-weight: bold;
            margin: 15px 0;
            transition: background-color 0.3s;
            padding: 10px 15px;
            border-radius: 4px;
        }

        .sidebar a:hover, .sidebar a.active {
            background-color: #0056b3;
        }

        .sidebar a .icon {
            margin-right: 10px;
            font-size: 1.2em;
        }

        .toggle-btn {
            position: fixed;
            top: 15px;
            left: 260px;
            background-color: #FFFFFF;
            color: black;
            border: none;
            padding: 10px;
            cursor: pointer;
            border-radius: 4px;
            transition: left 0.3s;
            z-index: 1100;
        }

        .sidebar.hide + .toggle-btn {
            left: 10px;
        }

        .main-content {
            margin-left: 250px;
            padding: 20px;
            background-color: #f9f9f9;
            flex: 1;
            transition: margin-left 0.3s;
            overflow-y: auto;
            min-height: 100vh;
        }

        .main-content.hide {
            margin-left: 0;
        }
        @media (max-width: 768px) {
            .sidebar {
                width: 200px;
            }

            .sidebar.hide {
                width: 0;
                padding: 20px 0;
            }

            .toggle-btn {
                left: 210px;
            }

            .sidebar.hide + .toggle-btn {
                left: 10px;
            }

            .main-content {
                margin-left: 200px;
            }

            .main-content.hide {
                margin-left: 0;
            }
        }

        @media (max-width: 480px) {
            .sidebar {
                width: 150px;
            }

            .toggle-btn {
                left: 160px;
            }

            .sidebar.hide + .toggle-btn {
                left: 10px;
            }

            .main-content {
                margin-left: 150px;
            }

            .main-content.hide {
                margin-left: 0;
            }

            .sidebar a {
                font-size: 0.9em;
            }

            .sidebar a .icon {
                margin-right: 5px;
            }
        }
    </style>

    <script>
        function toggleSidebar() {
            var sidebar = document.querySelector('.sidebar');
            var mainContent = document.querySelector('.main-content');
            var toggleBtn = document.querySelector('.toggle-btn');
            sidebar.classList.toggle('hide');
            mainContent.classList.toggle('hide');
            toggleBtn.classList.toggle('hide');
        }

        function loadContent(page) {
            fetch(page)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(html => {
                    document.getElementById("main-content").innerHTML = html;
                    setActiveLink(page);
                })
                .catch(error => {
                    console.error('Error loading content:', error);
                    document.getElementById("main-content").innerHTML = "<p>Error loading content.</p>";
                });
        }

        function setActiveLink(page) {
            var links = document.querySelectorAll('.sidebar a');
            links.forEach(link => {
                if (link.getAttribute('onclick').includes(page)) {
                    link.classList.add('active');
                } else {
                    link.classList.remove('active');
                }
            });
        }

        window.onload = function () {
            loadContent('dashboard.jsp');
        };
    </script>
</head>
<body>
    <div class="sidebar">
        <h1 class="sidebar-title">Admin Dashboard</h1>
        <a onclick="loadContent('dashboard.jsp')">
            <span class="icon">🏠</span>Dashboard
        </a>
        <a onclick="loadContent('viewStatistics.jsp')">
            <span class="icon">📈</span>View Statistics
        </a>
        <a onclick="loadContent('createElection.jsp')">
            <span class="icon">📝</span>Create Election
        </a>
        <a onclick="loadContent('manageVoters.jsp')">
            <span class="icon">👥</span>Manage Voters
        </a>
        <a onclick="loadContent('manageCandidates.jsp')">
            <span class="icon">👤</span>Manage Candidates
        </a>
        <a onclick="loadContent('settings.jsp')">
            <span class="icon">⚙️</span>Settings
        </a>
    </div>

    <button class="toggle-btn" onclick="toggleSidebar()">☰</button>

    <div id="main-content" class="main-content">
    </div>
</body>
</html>
