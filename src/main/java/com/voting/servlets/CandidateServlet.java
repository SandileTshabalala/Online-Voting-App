/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.voting.dao.CandidateDao;
import com.voting.implementation.CandidateDaoImpl;
import com.voting.models.Candidate;
/**
 *
 * @author USER
 */
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB threshold after which files will be written to disk
        maxFileSize = 1024 * 1024 * 50, // 50MB maximum file size
        maxRequestSize = 1024 * 1024 * 100 // 100MB maximum request size
)
public class CandidateServlet extends HttpServlet {

    private CandidateDao candidateDao = new CandidateDaoImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the electionId from the request
        String electionIdParam = request.getParameter("electionId");
        if (electionIdParam == null || electionIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Election ID is required");
            return;
        }

        int electionId = Integer.parseInt(electionIdParam);

        // Retrieve other candidate data
        String fullName = request.getParameter("fullName");
        String dateOfBirth = request.getParameter("dateOfBirth");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String partyAffiliation = request.getParameter("partyAffiliation");
        String position = request.getParameter("position");
        String slogan = request.getParameter("slogan");
        String biography = request.getParameter("biography");
        String qualifications = request.getParameter("qualifications");
        String manifestoTitle = request.getParameter("manifestoTitle");
        String manifestoText = request.getParameter("manifestoText");
        String endorsements = request.getParameter("endorsements");

        // File paths where files will be saved
        String imagesPath = "C:/Users/USER/Documents/NetBeansProjects/votes/src/main/webapp/voting/images/";
        String videosPath = "C:/Users/USER/Documents/NetBeansProjects/votes/src/main/webapp/voting/videos/";
        String documentsPath = "C:/Users/USER/Documents/NetBeansProjects/votes/src/main/webapp/voting/documents/";

        // Create and populate the Candidate object
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setDateOfBirth(dateOfBirth);
        candidate.setEmail(email);
        candidate.setPhoneNumber(phoneNumber);
        candidate.setAddress(address);
        candidate.setPartyAffiliation(partyAffiliation);
        candidate.setPosition(position);
        candidate.setSlogan(slogan);
        candidate.setBiography(biography);
        candidate.setQualifications(qualifications);
        candidate.setManifestoTitle(manifestoTitle);
        candidate.setManifestoText(manifestoText);
        candidate.setEndorsements(endorsements);
        candidate.setStatus("Pending"); // Default status

        try {
            // Check if the request is multipart
            if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
                // Save profile picture
                Part profilePicturePart = request.getPart("profilePicture");
                if (profilePicturePart != null && profilePicturePart.getSize() > 0) {
                    String profilePictureFileName = getFileName(profilePicturePart);
                    profilePicturePart.write(imagesPath + profilePictureFileName);
                    candidate.setprofilePictureUrl(imagesPath + profilePictureFileName);
                }

                // Save manifesto video
                Part manifestoVideoPart = request.getPart("manifestoVideo");
                if (manifestoVideoPart != null && manifestoVideoPart.getSize() > 0) {
                    String manifestoVideoFileName = getFileName(manifestoVideoPart);
                    manifestoVideoPart.write(videosPath + manifestoVideoFileName);
                    candidate.setManifestoVideoUrl(videosPath + manifestoVideoFileName);
                }

                // Save supporting documents
                Collection<Part> parts = request.getParts(); // Get all parts
                for (Part part : parts) {
                    if ("supportingDocuments".equals(part.getName())) {
                        if (part.getSize() > 0) {
                            String supportingDocumentsFileName = getFileName(part);
                            part.write(documentsPath + supportingDocumentsFileName); // Save the file to documents path
                            candidate.setSupportingDocumentsUrl(documentsPath + supportingDocumentsFileName); // Set the path in candidate object
                        }
                    }
                }
                // Register the candidate with electionId
                candidateDao.registerCandidate(candidate, electionId);
                HttpSession session = request.getSession();
                session.setAttribute("candidate", candidate); // Redirect to success page
                request.setAttribute("message", "Your application is under review. We will contact you via email. Status: Pending.");
                request.getRequestDispatcher("candidateSuccess.jsp").forward(request, response);
            } else {
                // Handle the case where the request is not multipart
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request must be multipart/form-data");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

// Utility method to extract file name from part
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            List<Candidate> candidates = candidateDao.getAllCandidates();
            if (candidates == null || candidates.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                out.write("{\"message\": \"No candidates found.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
//                String json = new Gson().toJson(Collections.singletonMap("candidates", candidates));
                String json = objectMapper.writeValueAsString(Collections.singletonMap("candidates", candidates));
                System.out.println("Response JSON: " + json);
                out.print(json);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"An error occurred while processing your request.\"}");
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}
