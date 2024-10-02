/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

/**
 *
 * @author USER
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.voting.dao.ElectionDao;
import com.voting.implementation.ElectionDaoImpl;
import com.voting.models.Election;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "GetAllElectionsServlet", urlPatterns = {"/GetAllElectionsServlet"})
public class GetAllElectionsServlet extends HttpServlet {

    private final ElectionDaoImpl electionDao = new ElectionDaoImpl();
    private static final Logger LOGGER = Logger.getLogger(GetAllElectionsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            List<Election> elections = electionDao.getAllElections();
            LOGGER.log(Level.INFO, "Fetched elections: {0}", elections);
//            
//            String jsonResponse = new Gson().toJson(elections);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(elections);
            LOGGER.log(Level.INFO, "Returning JSON response: {0}", jsonResponse);
            try {
                PrintWriter out = response.getWriter();
                out.write(jsonResponse);
                out.flush();
                LOGGER.log(Level.INFO, "Response written successfully");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error writing response", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error writing response.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while fetching elections", e);
            String errorResponse = "{\"error\": \"Error while fetching elections\"}";
            LOGGER.log(Level.INFO, "Returning error response: {0}", errorResponse);
            try {
                PrintWriter out = response.getWriter();
                out.write(errorResponse);
                out.flush();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Error writing error response", ex);
            }
        }
    }
}
