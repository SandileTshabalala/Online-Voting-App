<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>  
<%@ page import="java.util.List" %>
<%@ page import="com.voting.dao.CandidateDao" %>
<%@ page import="com.voting.dao.ElectionDao" %>
<%@ page import="com.voting.dao.ResultsDao" %>
<%@ page import="com.voting.implementation.CandidateDaoImpl" %>
<%@ page import="com.voting.implementation.ResultsDaoImpl" %>
<%@ page import="com.voting.implementation.ElectionDaoImpl" %>
<%@ page import="com.voting.models.Candidate" %>
<%@ page import="com.voting.models.Election" %>
<%@ page import="com.voting.models.Results" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    ElectionDao electionDao = new ElectionDaoImpl();
    List<Election> elections = electionDao.getAllElections();
    CandidateDao candidateDao = new CandidateDaoImpl();
    ResultsDao resultsDao = new ResultsDaoImpl();
    Results winningResults = null;

    boolean hasActiveElections = false;
    Election activeElection = null;

    if (elections != null && !elections.isEmpty()) {
        for (Election election : elections) {
            if ("active".equals(election.getStatus())) {
                hasActiveElections = true;
                activeElection = election;
                break;
            } else {
                // Calculate winning results for inactive elections
                if (winningResults == null || winningResults.getElectionId() != election.getId()) {
                    winningResults = resultsDao.calculateElectionResults(election.getId());
                }
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Election Results</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                background-color: #f9f9f9;
            }
            h1, h2 {
                text-align: center;
            }
            .countdown {
                font-size: 24px;
                color: #e74c3c;
                text-align: center;
            }
            .results {
                text-align: center;
            }
            .winner {
                font-weight: bold;
                color: #4CAF50;
            }
        </style>
    </head>
    <body>
        <%
            if (hasActiveElections) {
        %>
        <h1>Countdown to Election</h1>
        <div class="countdown" id="countdown"></div>

        <script>
          <% if (activeElection != null) { %>
        const startDate = new Date('<%= activeElection.getStartDate() %>');
        const endDate = new Date('<%= activeElection.getEndDate() %>');
        const countdownElement = document.getElementById('countdown');
        console.log("Start Date:", startDate);
        console.log("End Date:", endDate);
        console.log("Current Date:", new Date());

        function updateCountdown() {
            const now = new Date();
            let timeLeft;

            if (now < startDate) {
                // Countdown to start of the election
                timeLeft = startDate - now;
                countdownElement.innerText = "Election starts in: " + formatTime(timeLeft);
            } else if (now >= startDate && now < endDate) {
                // Countdown to end of the election
                timeLeft = endDate - now;
                countdownElement.innerText = "Voting ends in: " + formatTime(timeLeft);
            } else {
                // Election has ended
                countdownElement.innerText = "Voting has ended!";
                clearInterval(countdownInterval);
            }
        }

        function formatTime(milliseconds) {
            const totalSeconds = Math.floor(milliseconds / 1000);
            const days = Math.floor(totalSeconds / (3600 * 24));
            const hours = Math.floor((totalSeconds % (3600 * 24)) / 3600);
            const minutes = Math.floor((totalSeconds % 3600) / 60);
            const seconds = totalSeconds % 60;

            let timeString = '';
            if (days > 0) timeString += days + 'd ';
            if (hours > 0 || days > 0) timeString += hours + 'h ';
            if (minutes > 0 || hours > 0 || days > 0) timeString += minutes + 'm ';
            timeString += seconds + 's';

            return timeString;
        }

        const countdownInterval = setInterval(updateCountdown, 1000);
        updateCountdown();
    <% } else { %>
        document.getElementById('countdown').innerText = "No active elections at the moment.";
    <% } %>
        </script>
        <%
            } else {
        %>
        <h1>Election Results</h1>
        <div class="results">
            <h2>Winners:</h2>
            <%
            if (winningResults != null && winningResults.getCandidateVotes() != null) {
                List<Candidate> candidates = candidateDao.getCandidatesForElection(winningResults.getElectionId());
                int maxVotes = 0;
                for (Candidate candidate : candidates) {
                    int votes = winningResults.getCandidateVotes().getOrDefault(candidate.getId(), 0);
                    if (votes > maxVotes) {
                        maxVotes = votes;
                    }
                }
                for (Candidate candidate : candidates) {
                    int votes = winningResults.getCandidateVotes().getOrDefault(candidate.getId(), 0);
                    if (votes == maxVotes) {
            %>
            <div class="winner">
                <p><strong>Candidate:</strong> <%= candidate.getCandidateId() %> - <strong></strong> <%= candidate.getFullName() %> - <strong>Votes:</strong> <%= votes %> - <strong>Position:</strong> <%= candidate.getPosition() %></p>
            </div>
            <%
                    }
                }
            } else {
            %>
            <p>No results available.</p>
            <%
            }
            %>
        </div>
        <%
            }
        %>
    </body>
</html>
