/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.voting.dao;

/**
 *
 * @author USER
 */
import com.voting.models.Candidate;
import com.voting.models.Voter;
import java.util.List;

public interface VoterDao {
    void registerVoter(Voter voter) throws Exception;
    Voter authenticateVoter(String email, String password) throws Exception;
    Voter getVoterById(int id) throws Exception;
    Voter findVoterByEmail(String email) throws Exception;
    List<Voter> getAllVoters();
    void updateVoterStatus(int id, String status);
    void deleteVoter(int id);
    void generateVotingToken(Voter voter) throws Exception;
    void markTokenAsUsed(Voter voter) throws Exception;
    List<Voter> searchVoters(String query) throws Exception;
    Voter findVoterByToken(String token);
}