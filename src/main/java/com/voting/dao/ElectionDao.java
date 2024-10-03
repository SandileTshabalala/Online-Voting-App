/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.voting.dao;

import com.voting.models.Election;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface ElectionDao {

    void addElection(Election election) throws SQLException;
    List<Election> getAllElections() throws SQLException;
    Election getElectionById(int id) throws SQLException;
    List<String> getElectionPositions(int id) throws SQLException;
    void updateElection(Election election) throws SQLException;
    void deleteElection(int id) throws SQLException;
    void updateElectionStatus(int id, String status);
//    //dashboard
//    int getTotalElectionCount();
//    int getActiveElectionCount();
//    int getTotalVoteCount();
}
