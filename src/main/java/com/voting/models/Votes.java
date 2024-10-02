/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voting.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
public class Votes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "abstain", nullable = false)
    private boolean abstain;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    private String position;
//    @ManyToOne
//    @JoinColumn(name = "voterId", nullable = false)
//    private Voter voter;
    @Column(name = "votingToken", nullable = false, unique = true)
    private String votingToken;

    @ManyToOne
    @JoinColumn(name = "electionId", nullable = false)
    private Election election;

    @ManyToOne
    @JoinColumn(name = "candidateId", nullable = true)
    private Candidate candidate;

    public Votes() {
    }

    public Votes(int id, LocalDateTime timestamp, String votingToken, Election election, Candidate candidate) {
        this.id = id;
        this.timestamp = timestamp;
        this.votingToken = votingToken;
        this.election = election;
        this.candidate = candidate;
    }

    public int getId() {
        return id;
    }



    public boolean isAbstain() {
        return abstain;
    }

    public void setAbstain(boolean abstain) {
        this.abstain = abstain;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getvotingToken() {
        return votingToken;
    }

    public void setvotingToken(String votingToken) {
        this.votingToken = votingToken;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
