<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.voting.models.Election" %>
<%@ page import="com.voting.models.Voter" %>
<%@ page import="com.voting.models.Candidate" %>
<%@ page import="com.voting.implementation.CandidateDaoImpl" %>
<%@ page import="com.voting.implementation.ElectionDaoImpl" %>
<%@ page import="com.voting.dao.ElectionDao" %>
<%@ page import="com.voting.dao.CandidateDao" %>
<%
    Voter voter = (Voter) session.getAttribute("voter");
    
    if (voter == null) {
        response.sendRedirect("voterlogin.jsp"); 
        return;
    }
    
    String votingToken = voter.getVotingToken();
    
    ElectionDao electionDao = new ElectionDaoImpl();
    List<Election> elections = electionDao.getAllElections();

    if (elections == null || elections.isEmpty()) {
        response.sendRedirect("errorPage.jsp");
        return;
    }

    session.setAttribute("elections", elections);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vote</title>
    <style>
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    </style>
</head>
<body>
    <h1>Vote for Your Candidates</h1>

    <form action="SubmitVoteServlet" method="POST">
        <input type="hidden" name="votingToken" value="<%= votingToken %>">
        
        <% for (Election election : elections) { %>
            <h2>Election: <%= election.getName() %></h2>

            <% 
                // check  candidates
                List<String> positions = election.getPositionList();
                boolean hasCandidates = false; 
                
                for (String position : positions) {
                    CandidateDao candidateDao = new CandidateDaoImpl();
                    List<Candidate> candidates = candidateDao.getCandidatesForElectionAndPosition(election.getId(), position);
                    
                    // Only display position if there are candidates
                    if (candidates != null && !candidates.isEmpty()) {
                        hasCandidates = true; 
            %>
            <fieldset>
                <legend><%= position %></legend>

                <% for (Candidate candidate : candidates) { %>
                    <input type="radio" name="vote_<%= position.toLowerCase() %>" value="<%= candidate.getId() %>" required>
                    Vote for <%= candidate.getFullName() %> (Party: <%= candidate.getPartyAffiliation() %>) <br>
                <% } %>              
                <input type="radio" name="vote_<%= position.toLowerCase() %>" value="abstain" required> Abstain<br>
            </fieldset>
            <br>
            <% 
                    } 
                } 
                // If no candidates dont show 
                if (!hasCandidates) { 
            %>
            <p>No candidates available for any positions in this election.</p>
            <% } %>
        <% } %>

        <button type="submit">Submit Votes</button>
    </form>

    <h2>Your One-time Voting Key: 
        <span style="color: <%= voter.isTokenUsed() ? "red" : "green" %>;">
            <%= voter.getVotingToken() %>
        </span>
    </h2>
    <h2>Token <%= voter.isTokenUsed() ? "Used" : "Not Yet Used" %></h2>
</body>
</html>
