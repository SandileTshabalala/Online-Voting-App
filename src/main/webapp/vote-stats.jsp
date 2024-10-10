<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.voting.dao.ResultsDao" %>
<%@ page import="com.voting.dao.ElectionDao" %>
<%@ page import="com.voting.dao.CandidateDao" %>
<%@ page import="com.voting.implementation.ResultsDaoImpl" %>
<%@ page import="com.voting.implementation.ElectionDaoImpl" %>
<%@ page import="com.voting.implementation.CandidateDaoImpl" %>
<%@ page import="com.voting.models.Candidate" %>
<%@ page import="com.voting.models.Election" %>
<%@ page import="com.voting.models.Results" %>
<%
    Voter voter = (Voter) session.getAttribute("voter");

    if (voter == null) {
        response.sendRedirect("voterLogin.jsp");
        return;
    }

    String votingToken = voter.getVotingToken();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Voting Statistics</title>
    <style>
        /* Your CSS styles here */
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f9f9f9;
        }
        h1, h2 {
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 40px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: center;
        }
        .progress-bar {
            width: 100%;
            height: 20px;
            background-color: #f3f3f3;
            border-radius: 10px;
            overflow: hidden;
            margin: 0 auto;
            position: relative;
        }
        .progress-bar-fill {
            height: 100%;
            background-color: #4caf50;
            transition: width 0.5s ease-in-out;
        }
        .percentage {
            position: absolute;
            right: 5px;
            top: 0;
            font-size: 12px;
            color: white;
        }
        footer {
            background-color: #333;
            color: white;
            text-align: center;
            padding: 15px 0;
            position: relative;
            bottom: 0;
            box-shadow: 0 -4px 6px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
    <header>
        <h1>Voting Statistics</h1>
    </header>

    <nav>
        <section class="welcome-section">
            <ul class="nav-menu">
                <li><b>Welcome</b></li>
                <li><a href="home.jsp">Live Votes</a></li>
                <li><a href="vote.jsp">Vote Here</a></li>
                <li><a href="vote-stats.jsp">Voting Statistics</a></li>
                <li><a href="contact.html">Contact Us</a></li>
                <li><a href="about.html">About Us</a></li>
                <li><a href="logout.jsp" class="btn-logout">Logout</a></li>
            </ul>
        </section>
    </nav>

    <h1>Voting Statistics</h1>

    <%
        // Retrieve elections from the database
        ElectionDao electionDao = new ElectionDaoImpl();
        List<Election> elections = electionDao.getAllElections();
        CandidateDao candidateDao = new CandidateDaoImpl();
        ResultsDao resultsDao = new ResultsDaoImpl();

        if (elections != null && !elections.isEmpty()) {
            for (Election election : elections) {
                // Calculate results for the current election
                Results results = resultsDao.calculateElectionResults(election.getId());
                List<Candidate> candidates = candidateDao.getCandidatesForElection(election.getId());
    %>

    <!-- Display the election name -->
    <h2><%= election.getName() %></h2>

    <table>
        <thead>
            <tr>
                <th>Candidate</th>
                <th>Votes</th>
                <th>Status Bar</th>
                <th>Position</th>
            </tr>
        </thead>
        <tbody>
            <% if (candidates != null && !candidates.isEmpty()) {
                for (Candidate candidate : candidates) {
                    int voteCount = results.getCandidateVotes().getOrDefault(candidate.getId(), 0);
                    double votePercentage = results.getTotalVotes() > 0 ? (voteCount / (double) results.getTotalVotes()) * 100 : 0.0;
            %>
            <tr>
                <td>
                    <div class="candidate-info">
                        <div class="candidate-image">
                            <img src="<%= candidate.getprofilePictureUrl() != null ? candidate.getprofilePictureUrl() : "default-profile.png" %>" alt="<%= candidate.getFullName() %>'s Profile Picture">
                        </div>
                        <div class="candidate-details">
                            <p><strong><%= candidate.getFullName() %></strong></p>
                        </div>
                    </div>
                </td>
                <td><%= voteCount %></td>
                <td>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width: <%= votePercentage %>%;"></div>
                        <span class="percentage"><%= String.format("%.2f", votePercentage) %>%</span>
                    </div>
                </td>
                <td><%= candidate.getPosition() %></td>
            </tr>
            <% } } else { %>
            <tr>
                <td colspan="4">No candidates available</td>
            </tr>
            <% } %>
        </tbody>
    </table>

    <%
            } // End for loop for elections
        } else {
    %>
    <h2>No Elections Available</h2>
    <%
        } // End if check for elections
    %>
</body>
</html>
   <form action="SubmitVoteServlet" method="POST">
        <input type="hidden" name="votingToken" value="<%= votingToken %>">

        <table>
            <thead>
                <tr>
                    <th>Position</th>
                    <th>Candidate</th>
                    <th>Vote</th>
                </tr>
            </thead>
            <tbody>
                <% if (candidates != null && !candidates.isEmpty()) { %>
                <% for (Candidate candidate : candidates) { %>
                <tr>
                    <td><%= candidate.getPosition() %></td>
                    <td><%= candidate.getFullName() %> (Party: <%= candidate.getPartyAffiliation() %>)</td>
                    <td>
                        <input type="radio" name="<%= candidate.getPosition().toLowerCase() %>" value="<%= candidate.getCandidateId() %>" required> Vote for <%= candidate.getFullName() %>
                        <input type="radio" name="<%= candidate.getPosition().toLowerCase() %>" value="abstain" required> Abstain
                    </td>
                </tr>
                <% } %>
                <% } else { %>
                <tr>
                    <td colspan="3">No candidates available for voting.</td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <button type="submit">Submit Votes</button>
    </form>