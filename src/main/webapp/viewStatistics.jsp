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
<%@ page import="com.voting.models.Voter" %>
<%@ page import="com.voting.models.Results" %>
<%
    
    ElectionDao electionDao = new ElectionDaoImpl();
    List<Election> elections = electionDao.getAllElections();
    CandidateDao candidateDao = new CandidateDaoImpl();
    ResultsDao resultsDao = new ResultsDaoImpl();

    if (elections != null && !elections.isEmpty()) {
        for (Election election : elections) {
           
            Results results = resultsDao.calculateElectionResults(election.getId());
            List<Candidate> candidates = candidateDao.getCandidatesForElection(election.getId());
           
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Live Vote Count</title>
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
            .candidate-info {
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .candidate-image img {
                width: 100px;
                height: 100px;
                object-fit: cover;
                border-radius: 50%;
                margin-right: 20px;
            }
            .candidate-details {
                text-align: left;
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
            header {
                background-color: #4CAF50;
                color: white;
                padding: 20px 0;
                text-align: center;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            nav {
                padding: 20px 0;
            }
            .welcome-section {
                text-align: center;
            }
            .nav-menu {
                list-style-type: none;
                padding: 0;
                display: flex;
                justify-content: center;
            }
            .nav-menu li {
                margin: 0 15px;
            }
            .nav-menu a {
                text-decoration: none;
                color: #4CAF50;
                padding: 10px 15px;
                border: 2px solid transparent;
                border-radius: 5px;
                transition: background-color 0.3s, color 0.3s, border-color 0.3s;
            }
            .nav-menu a:hover {
                background-color: #4CAF50;
                color: white;
                border-color: #4CAF50;
            }
            .btn-logout {
                background-color: #e74c3c;
                color: white;
            }
            .btn-logout:hover {
                background-color: #c0392b;
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
        <script>
          const electionId = <%= election.getId() %>;
          const contextPath = '<%= request.getContextPath() %>';


          function fetchLiveResults() {
              fetch(`${contextPath}/LiveResultsServlet?electionId=${electionId}`)
                              .then(response => {
                                  if (!response.ok) {
                                      throw new Error('Network response was not ok: ' + response.statusText);
                                  }
                                  return response.json();
                              })
                              .then(data => {
                                  const candidates = data.candidates;
                                  const totalVotes = data.totalVotes;


                                  candidates.forEach(candidate => {
                                      const row = document.getElementById(`candidate-row-${candidate.id}`);
                                      if (row) {
                                          row.querySelector('.votesCount').innerText = candidate.votesCount;
                                          row.querySelector('.progress-bar-fill').style.width = `${candidate.votePercentage}%`;                                      
                                          row.querySelector('.percentage').innerText = (candidate.votePercentage).toFixed(2) + '%';
                                      }
                                  });


                                  document.getElementById('totalVotes').innerText = totalVotes;
                              })
                              .catch(error => {
                                  console.error('Error fetching live results:', error);
                              });
                  }

                  // Fetch live results every 5 seconds
                  setInterval(fetchLiveResults, 5000);


                  window.onload = fetchLiveResults;
        </script>

    </head>
    <body>
        <h1>Live Vote Count</h1>
        <!-- Display the election name -->

        <h2>Election Results: <%= election.getName() %></h2>
        <p>Total Votes: <span id="totalVotes"><%results.getTotalVotes(); %></span></p>

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
                <%                    
                    if (candidates != null && !candidates.isEmpty()) {
                        for (Candidate candidate : candidates) {
                            int voteCount = results.getCandidateVotes().getOrDefault(candidate.getId(), 0);
                            double votePercentage = results.getTotalVotes() > 0 ? (voteCount / (double) results.getTotalVotes()) * 100 : 0.0;
                            String formattedVotePercentage = String.format("%.2f", votePercentage);
                %>

                <tr id="candidate-row-<%= candidate.getId() %>">
                    <td>
                        <div class="candidate-info">
                            <div class="candidate-image">
                                <img src="<%= candidate.getprofilePictureUrl() != null ? candidate.getprofilePictureUrl() : "default-profile.png" %>" alt="<%= candidate.getFullName() %>'s Profile Picture" width="50" height="50">
                            </div>
                            <div class="candidate-details">
                                <p><strong>Full Name:</strong> <%= candidate.getFullName() %></p>
                                <p><strong>Party:</strong> <%= candidate.getPartyAffiliation() %></p>
                            </div>
                        </div>
                    </td>
                    <td class="votesCount"><%= voteCount %></td>
                    <td>
                        <div class="progress-bar">
                            <div class="progress-bar-fill" style="width: <%= votePercentage %>%;"></div>

                            <span class="percentage"><%= formattedVotePercentage %>%</span>

                        </div>
                    </td>
                    <td><%= candidate.getPosition() %></td>
                </tr>
                <% 
                    } 
                } else { 
                %>
                <tr>
                    <td colspan="4">No candidates available</td>
                </tr>
                <% 
                } 
                %>
            </tbody>
        </table>

        <% 
                } // End for loop for elections
            } else { 
        %>
        <p>No elections available</p>
        <% 
            } 
        %>

        <!-- Footer -->
        <footer>
            <p>&copy; 2024 Online Voting System. All Rights Reserved.</p>
        </footer>
    </body>
</html>
