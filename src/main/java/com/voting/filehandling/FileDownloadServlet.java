/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.voting.filehandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 *
 * @author USER
 */
public class FileDownloadServlet extends HttpServlet {

    private static final String IMAGES_PATH = "C:\\Users\\USER\\Documents\\NetBeansProjects\\votes\\src\\main\\webapp\\uploads\\voting\\images\\";
    private static final String VIDEOS_PATH = "C:\\Users\\USER\\Documents\\NetBeansProjects\\votes\\src\\main\\webapp\\uploads\\voting\\videos\\";
    private static final String DOCUMENTS_PATH = "C:\\Users\\USER\\Documents\\NetBeansProjects\\votes\\src\\main\\webapp\\uploads\\voting\\documents\\";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileType = request.getParameter("type");
        String fileName = request.getParameter("file");

        String filePath = null;

        // Decide file path based on the file type (image, video, document)
        if ("image".equalsIgnoreCase(fileType)) {
            filePath = IMAGES_PATH + fileName;
        } else if ("video".equalsIgnoreCase(fileType)) {
            filePath = VIDEOS_PATH + fileName;
        } else if ("document".equalsIgnoreCase(fileType)) {
            filePath = DOCUMENTS_PATH + fileName;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file type");
            return;
        }

        // Read the file and send it to the client
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            response.setContentType(getServletContext().getMimeType(file.getName()));
            response.setContentLength((int) file.length());

            try (FileInputStream in = new FileInputStream(file);
                 OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
        }
    }
}