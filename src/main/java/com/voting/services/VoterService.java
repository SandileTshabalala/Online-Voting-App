/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServiceLocator.java to edit this template
 */
package com.voting.services;

import com.voting.models.Voter;
import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

/**
 *
 * @author USER
 */

public class VoterService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/voting_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Saar5048?";

    public void registerVoter(Voter voter) throws Exception {
        if (findVoterByEmail(voter.getEmail()) != null) {
            throw new Exception("Email already registered.");
        }
        // Hash the password
        voter.setPassword(BCrypt.hashpw(voter.getPassword(), BCrypt.gensalt()));

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO voters (name, surname, email, studentNumber, phoneNumber, password) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, voter.getName());
            stmt.setString(2, voter.getSurName());
            stmt.setString(3, voter.getEmail());
            stmt.setString(4, voter.getStudentNumber());
            stmt.setString(5, voter.getPhoneNumber());
            stmt.setString(6, voter.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Registration failed: " + e.getMessage(), e);
        }
    }

    public Voter authenticateVoter(String email, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM voters WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Voter voter = new Voter();
                voter.setId(rs.getInt("id"));
                voter.setName(rs.getString("name"));
                voter.setSurName(rs.getString("surname"));
                voter.setEmail(rs.getString("email"));
                voter.setStudentNumber(rs.getString("studentNumber"));
                voter.setPhoneNumber(rs.getString("phoneNumber"));
                voter.setPassword(rs.getString("password"));

                // Check if password matches the hashed password
                if (BCrypt.checkpw(password, voter.getPassword())) {
                    return voter;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No voter found or authentication failed
    }

    public Voter findVoterByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM voters WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Voter voter = new Voter();
                voter.setId(rs.getInt("id"));
                voter.setName(rs.getString("name"));
                voter.setSurName(rs.getString("surname"));
                voter.setEmail(rs.getString("email"));
                voter.setStudentNumber(rs.getString("studentNumber"));
                voter.setPhoneNumber(rs.getString("phoneNumber"));
                voter.setPassword(rs.getString("password"));
                return voter;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No voter found
    }
}


