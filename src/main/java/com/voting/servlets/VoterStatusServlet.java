/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.dao.VoterDao;
import com.voting.implementation.VoterDaoImpl;
import com.voting.models.Voter;
import com.voting.services.EmailService;
import com.voting.services.VoterEmailService;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.*;

/**
 *
 * @author USER
 */
public class VoterStatusServlet extends HttpServlet {

    VoterDao voterDao;
    EmailService emailService; 
    VoterEmailService voterEmailService;
    
        @Override
    public void init() throws ServletException {
        super.init();
        voterDao = new VoterDaoImpl();

        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String username = "youremail"; 
        String password = "youpassword";
        
        if (username == null || password == null) {
            throw new ServletException("SMTP credentials are not set in environment variables.");
        }

        emailService = new EmailService(smtpHost, smtpPort, username, password);
        voterEmailService = new VoterEmailService(emailService);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String voterIdStr = request.getParameter("voterId");
        String action = request.getParameter("action");

        if (voterIdStr == null || voterIdStr.isEmpty() || action == null || action.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing voter ID or action.");
            return;
        }

        try {
            int voterId = Integer.parseInt(voterIdStr);
            Voter voter = voterDao.getVoterById(voterId);

            switch (action) {
                case "approve":
                    voterDao.updateVoterStatus(voterId, "Approved");
                    voterEmailService.sendVoterStatusEmail(voter.getEmail(), "Approved");
                    break;
                case "reject":
                    voterDao.updateVoterStatus(voterId, "Rejected");
                    voterEmailService.sendVoterStatusEmail(voter.getEmail(), "Rejected");
                    break;
                case "suspend":
                    voterDao.updateVoterStatus(voterId, "Suspended");
                    voterEmailService.sendVoterStatusEmail(voter.getEmail(), "Suspended");
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                    return;
            }

            response.sendRedirect("manageVoters.jsp");  

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid voter ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error managing voter.");
        }
    }

    
       @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String voterIdStr = request.getParameter("voterId");
        try {
            int voterId = Integer.parseInt(voterIdStr);
            voterDao.deleteVoter(voterId);
            response.getWriter().write("{\"message\": \"Voter deleted successfully\"}");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid voter ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while deleting voter.");
        }
    }

}
