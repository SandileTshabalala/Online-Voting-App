/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.voting.dao.ElectionDao;
import com.voting.implementation.ElectionDaoImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */

@WebServlet(name = "GetPositionsServlet", urlPatterns = {"/GetPositionsServlet"})
public class GetPositionsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GetPositionsServlet.class.getName());
    private final ElectionDaoImpl electionDao = new ElectionDaoImpl();

    @Override
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            int electionId = Integer.parseInt(request.getParameter("electionId"));
            List<String> positions = electionDao.getElectionPositions(electionId);
            LOGGER.log(Level.INFO, "Fetched positions for election ID {0}: {1}", new Object[]{electionId, positions});

            // Create JSON response with positions
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("positions", positions);
            
//            String json = new Gson().toJson(jsonResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(jsonResponse);

            PrintWriter out = response.getWriter();
            out.write(json);
            out.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while fetching positions", e);
            String errorResponse = "{\"error\": \"Error while fetching positions\"}";
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
