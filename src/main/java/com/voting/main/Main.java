/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voting.main;

import com.voting.dao.VoterDao;
import com.voting.implementation.VoterDaoImpl;
import com.voting.services.EmailService;
import com.voting.services.VoterEmailService;
import com.voting.models.Voter;
import java.util.List;

/**
 *
 * @author USER
 */
public class Main {
    public static void main(String[] args) {
        String smtpHost = "smtp.gmail.com"; 
        String smtpPort = "587"; // or 465 for SSL
        String username = "sandil.saar@gmail.com"; 
        String password = "mkizeevgsjkbvree"; 
        
        EmailService emailService = new EmailService(smtpHost, smtpPort, username, password);
        VoterEmailService voterEmailService = new VoterEmailService(emailService);
        
        VoterDao voterDao = new VoterDaoImpl();
        List<Voter> voters = voterDao.getAllVoters();       
        if (voters.isEmpty()) {
            System.out.println("No voters found.");
            return;
        }
        for (Voter voter : voters) {
            String voterEmail = voter.getEmail();
            String status = voter.getStatus(); 

            // Send the status email
            voterEmailService.sendVoterStatusEmail(voterEmail, status);
            System.out.println("Sent email to: " + voterEmail + " with status: " + status);
        }
    }
}