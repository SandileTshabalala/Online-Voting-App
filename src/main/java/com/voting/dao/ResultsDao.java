/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voting.dao;

import com.voting.models.Results;
import java.util.List;

public interface ResultsDao {
//    Results getResultsForElection(int electionId);
    Results calculateElectionResults(int electionId);
}