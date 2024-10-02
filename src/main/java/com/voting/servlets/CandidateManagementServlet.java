package com.voting.servlets;

import com.voting.dao.CandidateDao;
import com.voting.implementation.CandidateDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class CandidateManagementServlet extends HttpServlet {

    private CandidateDao candidateDao = new CandidateDaoImpl();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle candidate approval or rejection
        String action = request.getParameter("action");
        int candidateId = Integer.parseInt(request.getParameter("Id"));

        try {
            if ("approve".equals(action)) {
                candidateDao.approveCandidate(candidateId);
            } else if ("reject".equals(action)) {
                candidateDao.rejectCandidate(candidateId);
            }
            response.sendRedirect("manageCandidates.jsp"); 
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"An error occurred while processing your request.\"}");
            e.printStackTrace();
        }
    }
}