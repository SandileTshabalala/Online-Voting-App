/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.dao.CandidateDao;
import com.voting.implementation.ResultsDaoImpl;
import com.voting.models.Results;
import com.voting.dao.ResultsDao;
import com.voting.implementation.CandidateDaoImpl;
import com.voting.models.Candidate;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

//@WebServlet(name = "ResultsServlet", urlPatterns = {"/ResultsServlet"})
public class ResultsServlet extends HttpServlet {

    ResultsDao resultsDAO = new ResultsDaoImpl();
    CandidateDao candidateDAO = new CandidateDaoImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String electionIdStr = request.getParameter("electionId");
            if (electionIdStr == null || electionIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Election ID is missing.");
                return;
            }
            int electionId = Integer.parseInt(electionIdStr);
            List<Candidate> candidates = candidateDAO.getCandidatesForElection(electionId);
            Results results = resultsDAO.calculateElectionResults(electionId);

            // Calculate total votes
            int totalVotes = results.getCandidateVotes().values().stream().mapToInt(Integer::intValue).sum();

            // Assign votesCount and votePercentage to each candidate
            for (Candidate candidate : candidates) {
                int voteCount = results.getCandidateVotes().getOrDefault(candidate.getId(), 0);
                candidate.setVotesCount(voteCount);
                if (totalVotes > 0) {
                    double percentage = ((double) voteCount / totalVotes) * 100;
                    candidate.setVotePercentage(percentage);
                } else {
                    candidate.setVotePercentage(0.0);
                }
            }

            // Set candidates as a request attribute to forward to the JSP page
            request.setAttribute("candidates", candidates);
            request.setAttribute("totalVotes", totalVotes);

            // Forward to the results JSP page
            request.getRequestDispatcher("results.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Election ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving results.");
        }
    }
}
