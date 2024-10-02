package com.voting.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Results implements Serializable {
    private int electionId;
    private Map<Integer, Integer> candidateVotes; // Stores candidateId -> voteCount
    private int totalVotes;

    public Results() {}

    public Results(int electionId) {
        this.electionId = electionId;
        this.candidateVotes = new HashMap<>();
        this.totalVotes = 0;
    }

    public void addVote(int candidateId) {
        this.candidateVotes.put(candidateId, this.candidateVotes.getOrDefault(candidateId, 0) + 1);
        this.totalVotes++;
    }

    public double getVotePercentage(int candidateId) {
        if (totalVotes == 0) {
            return 0.0;
        }
        return (candidateVotes.getOrDefault(candidateId, 0) / (double) totalVotes) * 100;
    }

    public int getElectionId() {
        return electionId;
    }

    public Map<Integer, Integer> getCandidateVotes() {
        return candidateVotes;
    }

    public int getTotalVotes() {
        return totalVotes;
    }
}





//    public void addVote(int candidateId, int voteCount) {
//        this.candidateVotes.put(candidateId, voteCount);
//    }