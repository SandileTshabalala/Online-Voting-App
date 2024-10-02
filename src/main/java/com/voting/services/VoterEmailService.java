/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServiceLocator.java to edit this template
 */
package com.voting.services;

/**
 *
 * @author USER
 */
public class VoterEmailService {

    private EmailService emailService;

    public VoterEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendVoterStatusEmail(String voterEmail, String status) {
        String subject = "Voter Registration Status";
        String message = "Dear Voter,\n\nYour registration status is: " + status + ".\n\nThank you!";
        
        // Send the email using EmailService
        emailService.sendNotification(voterEmail, message);
    }
}
