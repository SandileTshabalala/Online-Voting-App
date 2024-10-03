/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voting.services;

/**
 *
 * @author USER
 */
public class CandidateEmailService {

    private EmailService emailService;

    public CandidateEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendCandidateStatusEmail(String candidateEmail, String status, String candidateId) {
        String subject = "Candidate Registration Status";
        String message = "Dear Candidate,\n\nYour Candidate ID is: " + candidateId +
                "\n\nYour registration status is: " + status + ".\n\nThank you!";

        // Send the email using EmailService
        emailService.sendNotification(candidateEmail, message);
    }
}


