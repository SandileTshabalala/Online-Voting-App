package com.voting.servlets;

import com.voting.dao.CandidateDao;
import com.voting.implementation.CandidateDaoImpl;
import com.voting.models.Candidate;
import com.voting.services.CandidateEmailService;
import com.voting.services.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class CandidateManagementServlet extends HttpServlet {

    private CandidateDao candidateDao;
    private EmailService emailService;
    private CandidateEmailService candidateEmailService;

    @Override
    public void init() throws ServletException {
        super.init();
        candidateDao = new CandidateDaoImpl();

        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String username = "youremail"; 
        String password = "yourpassword";

        if (username == null || password == null) {
            throw new ServletException("SMTP credentials are not set.");
        }

        emailService = new EmailService(smtpHost, smtpPort, username, password);
        candidateEmailService = new CandidateEmailService(emailService); // Fixed initialization
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle candidate approval or rejection
        String action = request.getParameter("action");
        String candidateIdStr = request.getParameter("Id");

        if (action == null || candidateIdStr == null || candidateIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action or candidate ID.");
            return;
        }

        try {
            int candidateId = Integer.parseInt(candidateIdStr);
            Candidate candidate = candidateDao.getCandidateById(candidateId);
            if ("approve".equals(action)) {
                candidateDao.approveCandidate(candidateId);
                candidateEmailService.sendCandidateStatusEmail(candidate.getEmail(), "Approved", candidate.getCandidateId());
            } else if ("reject".equals(action)) {
                candidateDao.rejectCandidate(candidateId);
                candidateEmailService.sendCandidateStatusEmail(candidate.getEmail(), "Rejected", candidate.getCandidateId());
            }
            response.sendRedirect("manageCandidates.jsp");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"An error occurred while processing your request.\"}");
            e.printStackTrace();
        }
    }
}
