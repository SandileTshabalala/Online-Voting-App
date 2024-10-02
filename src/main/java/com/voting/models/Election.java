/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voting.models;

/**
 *
 * @author USER
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "elections")
public class Election implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "startDate")
    private String startDate;
    @Column(name = "endDate")
    private String endDate;
    @Column(name = "status")
    private String status;
    @Column(name = "positions")
    private String positions;
    
    @JsonManagedReference
    @Expose(serialize = false)
    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Candidate> candidates;

    // Many-to-Many relationship with Voters
    @ManyToMany(mappedBy = "elections")
    @JsonIgnore // Using Jackson
    private List<Voter> voters;

    public Election() {
    }

    public Election(int id, String name, String description, String startDate, String endDate, String status, String positions, List<Candidate> candidates, List<Voter> voters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.positions = positions;
        this.candidates = candidates;
        this.voters = voters;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }
    // relationships

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public void setVoters(List<Voter> voters) {
        this.voters = voters;
    }
// Split positions stored as comma-separated string

    public List<String> getPositionList() {
        return Arrays.asList(positions.split(","));
    }

    // Convert List<String> to comma-separated string
    public void setPositionList(List<String> positionList) {
        this.positions = String.join(",", positionList);
    }
}
