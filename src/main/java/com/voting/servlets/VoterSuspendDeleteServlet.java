/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.dao.VoterDao;
import com.voting.implementation.VoterDaoImpl;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

/**
 *
 * @author USER
 */
public class VoterSuspendDeleteServlet extends HttpServlet {

    VoterDao voterDao = new VoterDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String voterIdStr = request.getParameter("voterId");
        String action = request.getParameter("action");

        try {
            int voterId = Integer.parseInt(voterIdStr);
            if ("suspend".equals(action)) {
                voterDao.updateVoterStatus(voterId, "Suspended");
            } else if ("delete".equals(action)) {
                voterDao.deleteVoter(voterId);
            }
            response.sendRedirect("manageVoters.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error managing voter.");
        }
    }
}
