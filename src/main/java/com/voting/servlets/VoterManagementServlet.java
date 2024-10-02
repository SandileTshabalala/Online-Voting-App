/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.dao.VoterDao;
import com.voting.implementation.VoterDaoImpl;
import com.voting.models.Voter;
import jakarta.servlet.RequestDispatcher;
import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.logging.*;


/**
 *
 * @author USER
 */
public class VoterManagementServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(VoterManagementServlet.class.getName());
    private VoterDao voterDao = new VoterDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("VoterManagementServlet doGet called.");
        String searchQuery = request.getParameter("search");
        List<Voter> voters;

        try {
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                voters = voterDao.searchVoters(searchQuery);
                LOGGER.log(Level.INFO, "Search Query: {0}, Voters Found: {1}", new Object[]{searchQuery, voters.size()});
            } else {
                voters = voterDao.getAllVoters();
                LOGGER.log(Level.INFO, "Total Voters Fetched: {0}", voters.size());
            }
            request.setAttribute("voters", voters);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching voters: ", e);
            request.setAttribute("voters", Collections.emptyList());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while fetching voters.");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("manageVoters.jsp");
        dispatcher.forward(request, response);
    }
}
