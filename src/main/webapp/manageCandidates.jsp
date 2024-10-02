<%@ page import="java.util.List" %> 
<%@ page import="com.voting.models.Candidate" %>
<%@ page import="com.voting.implementation.CandidateDaoImpl" %>
<%@ page import="jakarta.servlet.http.*"%>
<%@ page import="com.voting.dao.CandidateDao"%>
<%@ page import="java.util.Base64" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Manage Candidates</title>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid #ccc;
                padding: 8px;
                text-align: left;
            }
            img {
                width: 100px; /* Adjust the size as needed */
                height: auto;
            }
        </style>
        <script>
            function confirmAction(action, candidateName) {
                return alert("Are you sure you want to " + action + " " + candidateName + "?")
                 window.location.reload(); ;
            }
        </script>
    </head>
    <body>
        <h1>Manage Applications</h1>

        <% 
        CandidateDao candidateDao = new CandidateDaoImpl();
        List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidates");
        try {
            candidates = candidateDao.getAllCandidates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        %>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Party Affiliation</th>
                    <th>Position</th>
                    <th>Profile Picture</th>
                    <th>Status</th>
                    <th>Applications</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% 
                if (candidates != null && !candidates.isEmpty()) {                   
                    for (Candidate candidate : candidates) { 
                %>
                <tr>
                    <td><%= candidate.getCandidateId() %></td>
                    <td><%= candidate.getFullName() %></td>
                    <td><%= candidate.getPartyAffiliation() %></td>
                    <td><%= candidate.getPosition() %></td>
                    <td>
                        <% 
                        if (candidate.getProfilePicture() != null) {
                            String base64Image = Base64.getEncoder().encodeToString(candidate.getProfilePicture());
                        %>
                        <img src="data:image/png;base64,<%= base64Image %>" alt="<%= candidate.getFullName() %>'s Profile Picture" />
                        <% } else { %>
                        <p>No image available</p>
                        <% } %>
                    </td>
                    <td><%= candidate.getStatus() %></td>
                    <td><a href="candidateInformation.jsp?candidateId=<%= candidate.getId() %>">Candidate-Information</a></td>                    
                    <td>
                        <form method="post" action="CandidateManagementServlet" onsubmit="return confirmAction(this.action.value, '<%= candidate.getId() %>');">
                            <input type="hidden" name="Id" value="<%= candidate.getId() %>" />
                            <button type="submit" name="action" value="approve">Approve</button>
                            <button type="submit" name="action" value="reject">Reject</button>
                        </form>
                    </td>
                </tr>
                <% 
                    } 
                } else { 
                %>
                <tr>
                    <td colspan="8">No candidates found in the database.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </body>
</html>
