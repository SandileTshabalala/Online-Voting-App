/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.implementation.VoterDaoImpl;
import com.voting.models.Voter;

import jakarta.servlet.http.*;
import java.io.IOException;

public class VoteServlet extends HttpServlet {

    private VoterDaoImpl voterDao = new VoterDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String votingToken = request.getParameter("votingToken");
        String candidateId = request.getParameter("candidateId"); // The ID of the candidate being voted for

        // Logic to verify the token and store the vote anonymously
        try {
            Voter voter = voterDao.findVoterByEmail(request.getParameter("email")); // Get voter from session or request
            if (voter != null && !voter.isTokenUsed() && voter.getVotingToken().equals(votingToken)) {
                // Logic to store the vote (e.g., save in a Votes table)
                
                // Mark token as used
                voterDao.markTokenAsUsed(voter);
                response.sendRedirect("voteSuccess.html"); // Redirect to success page
            } else {
                response.sendRedirect("voteFailed.html"); // Redirect to failed vote page
            }
        } catch (Exception e) {
            response.sendRedirect("voteFailed.html"); // Redirect to failed vote page
        }
    }
}
