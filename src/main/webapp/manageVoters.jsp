<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.voting.models.Voter" %>
<%@ page import="com.voting.implementation.VoterDaoImpl" %>
<%@ page import="jakarta.servlet.http.*" %>
<%@ page import="com.voting.dao.VoterDao" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Voter Management</title>
        <style>
            .search-bar{
                float:  right;
            }
            h1{
                text-align: center;
            }
            table {
                width: auto;
                border-spacing: 0 5px;
                
                background-color: #fff;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                
                 width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            th, td {
                padding: 10px 12px;
                text-align: left;
                border: 1px solid #ddd;
            }
            th {
                background-color: #18181B;
                color: white;
            }
            tr:hover {
                background-color: #f1f1f1;
            }
            .action-buttons {
/*                display: inline-flex;*/
/*                gap: 5px;*/
            }
            button {
                background-color: #18181B;
                color: white;
                border: none;
                margin: 3px;
                padding: 5px 5px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
            }
            button:hover {
                background-color: #18181B;
            }
            .candidate-info {
                display: flex;
                align-items: center;
            }
            .candidate-image {
                margin-right: 10px;
            }

            .candidate-image img {
                width: 40px;
                height: auto;
                border-radius: 50%;
            }
            .candidate-details p {
                margin: 0;
                font-size: 14px;
                color: #333;
            }

            @media (max-width: 600px) {
                th, td {
                    padding: 8px;
                }

                .candidate-image img {
                    width: 35px;
                }

                .candidate-details p {
                    font-size: 12px;
                }
            }
        </style>
        <script>
            function confirmDelete(id) {
                if (confirm("Are you sure you want to delete this voter?")) {
                    fetch('VoterStatusServlet?voterId=' + id, {
                        method: 'DELETE'
                    }).then(response => response.json())
                            .then(data => {
                                alert(data.message);
                                window.location.reload(); // Refresh the page
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert("An error occurred while deleting.");
                            });
                }
            }
        </script>
    </head>
    <body>
        <h1>Voter Management</h1>

        <div class="search-bar">
            <form action="VoterManagementServlet" method="get">
                <input type="text" name="search" placeholder="Search voters by name or email">
                <button type="submit">Search</button>
            </form>
        </div>

        <% 
            VoterDao voterDao = new VoterDaoImpl();
            List<Voter> voters = (List<Voter>) request.getAttribute("voters");
            try {
                voters = voterDao.getAllVoters();
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>SurName</th>
                    <th>Email</th>
                    <th>Student Number</th>
                    <th>Phone Number</th>
                    <th>Status</th>
                    <th>Registration Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (voters != null && !voters.isEmpty()) {
                        for (Voter voter : voters) {
                %>
                <tr>
                    <td><%= voter.getId() %></td>
                    <td><%= voter.getName() %></td>
                    <td><%= voter.getSurName() %></td>
                    <td><%= voter.getEmail() %></td>
                    <td><%= voter.getStudentNumber() %></td>
                    <td><%= voter.getPhoneNumber() %></td>
                    <td><%= voter.getStatus() %></td>
                    <td><%= voter.getFormattedRegistrationDate() %></td>
                    <td class="action-buttons" >
                        <form action="VoterStatusServlet" method="post">
                            <input type="hidden" name="voterId" value="<%= voter.getId() %>"> 
                            <button type="submit" name="action" value="approve">Approve</button><br>
                            <button type="submit" name="action" value="reject">Reject</button><br>
                            <button type="submit" name="action" value="suspend">Suspend</button>
                        </form>

                        <button onclick="confirmDelete(<%= voter.getId() %>)">Delete</button>
                    </td>

                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="9">No voters found.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

    </body>
</html>
