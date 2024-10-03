<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.voting.models.Election"%>
<%@page import="jakarta.servlet.http.*"%>
<%@page import="com.voting.dao.ElectionDao"%>
<%@page import="com.voting.implementation.ElectionDaoImpl"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Election List</title>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
            }
            table, th, td {
                border: 1px solid black;
            }
            th, td {
                padding: 10px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
        </style>
        <script>
            function confirmDelete(id) {
                if (confirm("Are you sure you want to delete this election?")) {
                    fetch('ElectionServlet?id=' + id, {
                        method: 'DELETE'
                    }).then(response => {
                        if (response.ok) {
                            alert("Election deleted successfully!");
                            window.location.reload(); // Refresh the page
                        } else {
                            alert("Failed to delete the election.");
                        }
                    }).catch(error => {
                        console.error('Error:', error);
                        alert("An error occurred.");
                    });
                }
            }

            function confirmUpdate(id, status) {
                if (confirm("Are you sure you want to update the status of this election?")) {
                    fetch('ElectionServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: 'action=updateStatus&id=' + id + '&status=' + status
                    }).then(response => {
                        if (response.ok) {
                            alert("Election status updated successfully!");
                            window.location.reload(); // Refresh the page
                        } else {
                            alert("Failed to update election status.");
                        }
                    }).catch(error => {
                        console.error('Error:', error);
                        alert("An error occurred.");
                    });
                }
            }
        </script>
    </head>
    <body>
        <h1>Manage Elections</h1>

        <%
            ElectionDao electionDao = new ElectionDaoImpl();
            List<Election> elections = (List<Election>) request.getAttribute("elections");
            try {
                elections = electionDao.getAllElections();
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Status</th>
                    <th>Positions</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (elections != null && !elections.isEmpty()) {
                        for (Election election : elections) { 
                            String currentStatus = election.getStatus();
                            String endDate = election.getEndDate();
                            String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

                            // close the election if the end date has passed
                            if (endDate != null && endDate.compareTo(currentDate) < 0 && !currentStatus.equals("Inactive")) {
                                electionDao.updateElectionStatus(election.getId(), "Inactive");
                                currentStatus = "Inactive"; 
                            }
                %>
                <tr>
                    <td><%= election.getId()%></td>
                    <td><%= election.getName()%></td>
                    <td><%= election.getDescription()%></td>
                    <td><%= election.getStartDate()%></td>
                    <td><%= election.getEndDate()%></td>
                    <td><%= currentStatus %></td>
                    <td><%= election.getPositions()%></td>
                    <td>
                        <form action="ElectionServlet" method="post">
                            <input type="hidden" name="id" value="<%= election.getId()%>">
                            <input type="submit" value="Edit">
                        </form>
                        <button onclick="confirmDelete(<%= election.getId()%>)">Delete</button>
                        <%-- Update Status Button --%>
                        <% if (!currentStatus.equals("Inactive")) { %>
                        <button onclick="confirmUpdate(<%= election.getId() %>, '<%= currentStatus.equals("Active") ? "Inactive" : "Active" %>')">
                            Set <%= currentStatus.equals("Active") ? "Inactive" : "Active" %>
                        </button>
                        <% } else { %>
                        <button disabled>Closed</button>
                        <% } %>
                    </td>
                </tr>
                <%  }
                } else { %>
                <tr>
                    <td colspan="8">No elections found.</td>
                </tr>
                <% }%>
            </tbody>
        </table>
        <a href="admin.jsp"><button>Go back</button></a>
    </body>
</html>
