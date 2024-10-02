/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.voting.dao;

import com.voting.models.Votes;
import java.util.List;



public interface VotesDao {
    void saveVote(Votes vote);
    List<Votes> getVotesForElection(int electionId);
    boolean hasVoted(int electionId, int voterId);
    Votes findVoteByVotingTokenElectionPosition(String votingToken, int electionId, String position);;
}
