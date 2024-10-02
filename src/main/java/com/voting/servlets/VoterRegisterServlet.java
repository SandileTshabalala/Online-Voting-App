/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.dao.ElectionDao;
import com.voting.models.Voter;
import com.voting.dao.VoterDao;
import com.voting.implementation.ElectionDaoImpl;
import com.voting.implementation.VoterDaoImpl;
import com.voting.models.Election;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//@WebServlet("/VoterRegisterServlet")
public class VoterRegisterServlet extends HttpServlet {

    private VoterDao voterDao = new VoterDaoImpl(); // Use DAO implementation
    private ElectionDao electionDao = new ElectionDaoImpl(); // Add Election DAO

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = 0;
        String name = request.getParameter("name");
        String surName = request.getParameter("surName");
        String email = request.getParameter("email");
        String studentNumber = request.getParameter("studentNumber");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String status = "Pending";
        Timestamp registrationDate = new Timestamp(System.currentTimeMillis());
        String votingToken = null;
        boolean isTokenUsed = false;

        if (name == null || surName == null || email == null || studentNumber == null || phoneNumber == null || password == null || confirmPassword == null) {
            request.setAttribute("errorMessage", "Form parameters are missing.");
            request.getRequestDispatcher("voterRegister.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("voterRegister.jsp").forward(request, response);
            return;
        }

        try {
            // Fetch elections the voter is applying for
            String[] electionIds = request.getParameterValues("elections");
            List<Election> elections = new ArrayList<>();

            if (electionIds != null) {
                for (String electionId : electionIds) {
                    Election election = electionDao.getElectionById(Integer.parseInt(electionId));
                    elections.add(election);
                }
            }

            // Create the Voter object
            Voter voter = new Voter(id, name, surName, email, studentNumber, phoneNumber,
                    password, status, registrationDate, votingToken, isTokenUsed, elections);

            // Register the voter
            voterDao.registerVoter(voter);
            voterDao.generateVotingToken(voter);
            
            HttpSession session = request.getSession();
            session.setAttribute("voter", voter);
            // Send verification email with the token (implement your email logic here)
            response.sendRedirect("registrationSuccess.jsp");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("voterRegister.jsp").forward(request, response);
        }
    }
}
