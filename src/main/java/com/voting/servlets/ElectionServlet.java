/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.voting.dao.ElectionDao;
import com.voting.implementation.ElectionDaoImpl;
import com.voting.models.Election;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.time.LocalDate;

/**
 *
 * @author USER
 */
public class ElectionServlet extends HttpServlet {

    private ElectionDao electionDao = new ElectionDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            // Handle election creation
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String status = request.getParameter("status"); 
            String positions = request.getParameter("positions");

            Election election = new Election();
            election.setName(name);
            election.setDescription(description);
            election.setStartDate(LocalDate.parse(startDate));
            election.setEndDate(LocalDate.parse(endDate));
            election.setStatus(status);
            election.setPositions(positions);

            try {
                electionDao.addElection(election);
                response.sendRedirect("elections.jsp");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while creating the election: " + e.getMessage());
            }

        } else if ("updateStatus".equals(action)) {
            // Handle status update
            int id = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");

            try {
                electionDao.updateElectionStatus(id, status);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating status");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Election> elections = electionDao.getAllElections();           
            LocalDate currentDate = LocalDate.now();
            for (Election election : elections) {
                if (LocalDate.parse(election.getEndDate()).isBefore(currentDate) && !"inactive".equals(election.getStatus())) {
                    election.setStatus("inactive");
                    electionDao.updateElectionStatus(election.getId(), "inactive");
                }
            }

            request.setAttribute("elections", elections);
            RequestDispatcher dispatcher = request.getRequestDispatcher("elections.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("elections", Collections.emptyList());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while fetching elections.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("id"));

        try {
            electionDao.deleteElection(electionId);
            response.getWriter().write("{\"message\": \"Election deleted successfully\"}");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while deleting election.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String status = request.getParameter("status"); // Expecting "active" or "inactive"
        String positions = request.getParameter("positions");

        Election election = new Election();
        election.setId(electionId);
        election.setName(name);
        election.setDescription(description);
        election.setStartDate(LocalDate.parse(startDate));
        election.setEndDate(LocalDate.parse(endDate));
        election.setStatus(status); // Set the status
        election.setPositions(positions);

        try {
            electionDao.updateElection(election);
            response.getWriter().write("{\"message\": \"Election updated successfully\"}");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while updating the election.");
        }
    }
}
