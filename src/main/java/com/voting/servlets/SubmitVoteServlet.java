package com.voting.servlets;

import com.voting.dao.CandidateDao;
import com.voting.implementation.VotesDaoImpl;
import com.voting.models.Votes; // Correct import for the Votes class

import java.io.IOException;
import java.time.LocalDateTime;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.voting.dao.VotesDao;
import com.voting.implementation.CandidateDaoImpl;
import com.voting.implementation.VoterDaoImpl;
import com.voting.models.Candidate;
import com.voting.models.Election;
import com.voting.models.Voter;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.*;
import java.util.*;

@WebServlet(name = "SubmitVoteServlet", urlPatterns = {"/SubmitVoteServlet"})
public class SubmitVoteServlet extends HttpServlet {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VotingSystemPU");
    private final VoterDaoImpl voterDao = new VoterDaoImpl();
    private final VotesDaoImpl votesDao = new VotesDaoImpl();
    private final CandidateDaoImpl candidateDao = new CandidateDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        boolean tokenUsed = false;

        try {
            HttpSession session = request.getSession();
            String votingToken = request.getParameter("votingToken");

            if (votingToken == null || votingToken.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing voting token.");
                return;
            }

            // Validate the voting token
            Voter voter = voterDao.findVoterByToken(votingToken);
            if (voter == null || voter.isTokenUsed()) {
                response.sendRedirect("voterLogin.jsp");
                return;
            }

            // Get the elections from the session
            
            List<Election> elections = (List<Election>) session.getAttribute("elections");
            if (elections == null || elections.isEmpty()) {
                throw new IllegalArgumentException("Invalid election information.");
            }

            for (Election election : elections) {
                // For each election, we handle the positions
                List<String> positions = election.getPositionList();

                for (String position : positions) {
                    String vote = request.getParameter("vote_" + position.toLowerCase());
                    System.out.println("Position: " + position + ", Submitted Vote: " + vote);

                    if (vote == null || vote.trim().isEmpty()) {
                        System.out.println("No vote submitted for position: " + position);
                        continue;
                    }
                       // Check if a vote for this election and position already exists for this votingToken
                    Votes existingVote = votesDao.findVoteByVotingTokenElectionPosition(votingToken, election.getId(), position);
                    if (existingVote != null) {
                        System.out.println("Vote already submitted for position: {0}" + position);
                        continue;  // Skip if already voted for this position
                    }
                    Votes newVote = new Votes();
                    newVote.setvotingToken(votingToken);
                    newVote.setElection(election);
                    newVote.setPosition(position);
                    newVote.setTimestamp(LocalDateTime.now());

                    if (vote.equals("abstain")) {
                        newVote.setCandidate(null);
                        newVote.setAbstain(true);
                    } else {
                        try {
                            int candidateId = Integer.parseInt(vote);
                            Candidate selectedCandidate = em.find(Candidate.class, candidateId);
                            if (selectedCandidate == null) {
                                throw new IllegalArgumentException("Invalid candidate ID.");
                            }
                            newVote.setCandidate(selectedCandidate);
                            newVote.setAbstain(false);
                        } catch (NumberFormatException e) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid candidate ID format.");
                            return;
                        }
                    }

                    // Save the vote
                    try {
                        votesDao.saveVote(newVote);
                        tokenUsed = true;
                         System.out.println( "Vote saved successfully for position: {0}"+ position);
                    } catch (Exception e) {
                        System.out.println("Error saving vote for position: " + position + e);
                        response.sendRedirect("voteFailed.html"); // Send to failed page
                        return;
                    }
                }
            }

            // Mark the voter's token as used only if a vote was saved
            if (tokenUsed) {
                voterDao.markTokenAsUsed(voter);
                  System.out.println("Voting token marked as used for token: {0}" + votingToken);
                response.sendRedirect("thankYou.jsp");
            } else {
                response.sendRedirect("voteFailed.html");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid vote data.");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing vote: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null) {
            emf.close();
        }
    }
}
