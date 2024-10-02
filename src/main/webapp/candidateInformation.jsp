<%@ page import="java.util.Base64" %>
<%@ page import="com.voting.models.Candidate" %>
<%@ page import="com.voting.implementation.CandidateDaoImpl" %>
<%@ page import="com.voting.dao.CandidateDao" %>
<%@ page import="jakarta.servlet.http.*"%>
<%@ page import="jakarta.servlet.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Candidate Information</title>
    <style>
        .candidate-info {
            margin: 20px;
        }
        img {
            width: 150px; /* Adjust the size as needed */
            height: auto;
        }
    </style>
</head>
<body>
<%
    // Retrieve the candidate ID from the request
    String candidateIdParam = request.getParameter("candidateId");
    Candidate candidate = null; // Initialize candidate variable

    try {
        // Check if candidateIdParam is not null and not empty
        if (candidateIdParam != null && !candidateIdParam.isEmpty()) {
            int candidateId = Integer.parseInt(candidateIdParam); // Convert to int
            CandidateDao candidateDao = new CandidateDaoImpl();
            candidate = candidateDao.getCandidateById(candidateId); // Fetch candidate by ID
        }
    } catch (NumberFormatException e) {
        out.println("<p>Error: Invalid candidate ID format.</p>"); // Handle invalid format
    } catch (Exception e) {
        e.printStackTrace(); // Log the exception for debugging
        out.println("<p>Error retrieving candidate information.</p>");
    }

    // Check if the candidate was found
    if (candidate != null) {
%>
    <div class="candidate-info">
        <h1>Candidate Information</h1>
        <p><strong>ID:</strong> <%= candidate.getCandidateId() %></p>
        <p><strong>Full Name:</strong> <%= candidate.getFullName() %></p>
        <p><strong>Email:</strong> <%= candidate.getEmail() %></p>
        <p><strong>Phone Number:</strong> <%= candidate.getPhoneNumber() %></p>
        <p><strong>Party Affiliation:</strong> <%= candidate.getPartyAffiliation() %></p>
        <p><strong>Position:</strong> <%= candidate.getPosition() %></p>
        <p><strong>Status:</strong> <%= candidate.getStatus() %></p>
        <p><strong>Biography:</strong> <%= candidate.getBiography() %></p>

        <h2>Profile Picture:</h2>
        <%
        if (candidate.getProfilePicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(candidate.getProfilePicture());
        %>
            <img src="data:image/png;base64,<%= base64Image %>" alt="<%= candidate.getFullName() %>'s Profile Picture" />
        <% } else { %>
            <p>No image available</p>
        <% } %>

        <h2>Supporting Documents:</h2>
        <%
        String documentUrl = candidate.getSupportingDocumentsUrl(); // Supporting document is a single URL string
        if (documentUrl != null && !documentUrl.isEmpty()) {
        %>
            <li><a href="<%= documentUrl %>" target="_blank">Download Document</a></li>
        <% } else { %>
            <p>No supporting documents available</p>
        <% } %>

        <h2>Manifesto:</h2>
        <p><%= candidate.getManifestoText() %></p>

        <button onclick="window.history.back()">Back</button>
    </div>
<%
    } else {
%>
    <h1>Candidate Not Found</h1>
    <p>The candidate information could not be found. Please check the candidate ID.</p>
    <button onclick="window.history.back()">Back</button>
<%
    }
%>
</body>
</html>
