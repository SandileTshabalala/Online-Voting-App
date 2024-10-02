/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.models.Voter;
import com.voting.dao.VoterDao;
import com.voting.implementation.VoterDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;


import java.io.IOException;

//@WebServlet(name = "VoterLoginServlet", urlPatterns = {"/VoterLoginServlet"})
public class VoterLoginServlet extends HttpServlet {

    private VoterDao voterDAO = new VoterDaoImpl(); 

protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    try {
        Voter voter = voterDAO.authenticateVoter(email, password);

        if (voter != null) {
            if (!"Approved".equals(voter.getStatus())) {
                request.setAttribute("errorMessage", "Your registration is" + voter.getStatus());
                request.getRequestDispatcher("voterLogin.jsp").forward(request, response);
                return;
            }


            HttpSession session = request.getSession();
            session.setAttribute("voter", voter);
            response.sendRedirect("home.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid email or password");
            request.getRequestDispatcher("voterLogin.jsp").forward(request, response);
        }
    } catch (Exception e) {
        request.setAttribute("errorMessage", "Login failed: " + e.getMessage());
        request.getRequestDispatcher("voterLogin.jsp").forward(request, response);
    }
}

}

//              HttpSession session = request.getSession();
//              session.setAttribute("voterId", voter.getId());