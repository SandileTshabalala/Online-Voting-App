/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.voting.dao.CandidateDao;
import com.voting.dao.ResultsDao;
import com.voting.implementation.CandidateDaoImpl;
import com.voting.implementation.ResultsDaoImpl;
import com.voting.models.Candidate;
import com.voting.models.Results;
/**
 *
 * @author USER
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "LiveResultsServlet", urlPatterns = {"/LiveResultsServlet"})
public class LiveResultsServlet extends HttpServlet {

    private CandidateDao candidateDAO = new CandidateDaoImpl();
    private ResultsDao resultsDAO = new ResultsDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper(); 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String electionIdStr = request.getParameter("electionId");
            if (electionIdStr == null || electionIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Election ID is missing.");
                return;
            }
            int electionId = Integer.parseInt(electionIdStr);

            List<Candidate> candidates = candidateDAO.getCandidatesForElection(electionId);
            Results results = resultsDAO.calculateElectionResults(electionId);

            if (candidates == null || candidates.isEmpty() || results == null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(Map.of("message", "No live results available.")));           
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No results or candidates found for the election.");
                return;
            }

            int totalVotes = results.getCandidateVotes().values().stream().mapToInt(Integer::intValue).sum();

            List<Map<String, Object>> candidatesData = candidates.stream().map(candidate -> {
                int voteCount = results.getCandidateVotes().getOrDefault(candidate.getId(), 0);
                double votePercentage = totalVotes > 0 ? ((double) voteCount / totalVotes) * 100 : 0.0;

                
                Map<String, Object> candidateData = new HashMap<>();
                candidateData.put("id", candidate.getId());
                candidateData.put("votesCount", voteCount);
                candidateData.put("votePercentage", votePercentage);

                return candidateData;
            }).collect(Collectors.toList());

           
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("candidates", candidatesData);
            responseData.put("totalVotes", totalVotes);

            // Convert to JSON using Jackson
            String json = objectMapper.writeValueAsString(responseData);

            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Election ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving results.");
        }
    }
}

