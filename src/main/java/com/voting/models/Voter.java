/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voting.models;

/**
 *
 * @author USER
 */
import java.text.SimpleDateFormat;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voters")
public class Voter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "surName")
    private String surName;

    @Column(unique = true)
    private String email;
    private String studentNumber;
    private String phoneNumber;
    private String password;
    private String status;

    @Column(name = "registrationDate")
    private Timestamp registrationDate;

    private String votingToken; // Token for voting
    private boolean isTokenUsed;

    @ManyToMany
    @JoinTable(
        name = "voter_elections",
        joinColumns = @JoinColumn(name = "voterId"),
        inverseJoinColumns = @JoinColumn(name = "electionId")
    )
    private List<Election> elections = new ArrayList<>(); // Initialize to avoid NullPointerException

    public Voter() {}

    public Voter(int id, String name, String surName, String email, String studentNumber, 
                 String phoneNumber, String password, String status, Timestamp registrationDate, 
                 String votingToken, boolean isTokenUsed, List<Election> elections) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.email = email;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = status;
        this.registrationDate = registrationDate;
        this.votingToken = votingToken;
        this.isTokenUsed = isTokenUsed;
        this.elections = elections;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getFormattedRegistrationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Change format as needed
        return sdf.format(registrationDate);
    }

    public String getVotingToken() {
        return votingToken;
    }

    public void setVotingToken(String votingToken) {
        this.votingToken = votingToken;
    }

    public boolean isTokenUsed() {
        return isTokenUsed;
    }

    public void setTokenUsed(boolean isTokenUsed) {
        this.isTokenUsed = isTokenUsed;
    }

    public List<Election> getElections() {
        return elections;
    }

    public void setElections(List<Election> elections) {
        this.elections = elections;
    }
    
    
}
