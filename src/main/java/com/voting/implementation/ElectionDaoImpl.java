/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.voting.implementation;

import com.voting.dao.ElectionDao;
import com.voting.models.Election;
import java.sql.*;
import java.util.*;
import jakarta.persistence.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class ElectionDaoImpl implements ElectionDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VotingSystemPU");
    private static final Logger LOGGER = Logger.getLogger(ElectionDaoImpl.class.getName());

  @Override
public void addElection(Election election) {
    // Validation
    if (election == null || election.getName() == null || election.getName().isEmpty()) {
        throw new IllegalArgumentException("Election name is required.");
    }

    System.out.println("Adding election: " + election.getName());
    EntityManager entityManager = emf.createEntityManager();
    try {
        entityManager.getTransaction().begin();
        entityManager.persist(election);
        entityManager.getTransaction().commit();
        System.out.println("Election added successfully.");
    } catch (Exception e) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback(); // Rollback if there's an exception
            System.out.println("Transaction rolled back due to an error.");
        }
        throw new RuntimeException("Failed to add election: " + election.getName(), e); // Throwing a custom exception
    } finally {
        entityManager.close();
    }
}


    @Override
    public List<Election> getAllElections() {
        EntityManager entityManager = emf.createEntityManager();
        List<Election> elections = new ArrayList<>();

        try {
            elections = entityManager.createQuery("SELECT e FROM Election e", Election.class).getResultList();
        } catch (PersistenceException e) {
            // Log the exception
            System.err.println("Error while fetching elections: " + e.getMessage());
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return elections != null ? elections : Collections.emptyList();
    }

    @Override
    public Election getElectionById(int id) {
        EntityManager entityManager = emf.createEntityManager();
        Election election = null;
        try {
            election = entityManager.find(Election.class, id);
        } finally {
            entityManager.close();
        }
        return election;
    }

//    @Override
//    public List<String> getElectionPositions(int electionId) {
//        EntityManager entityManager = emf.createEntityManager();
//        List<String> positions = new ArrayList<>();
//        try {
//            Election election = entityManager.find(Election.class, electionId);
//            if (election != null) {
//                // Assuming positions is a comma-separated string in the Election entity
//                String positionsStr = election.getPositions();
//                if (positionsStr != null && !positionsStr.isEmpty()) {
//                    positions = Arrays.asList(positionsStr.split(","));
//                }
//            }
//        } finally {
//            entityManager.close();
//        }
//        return positions;
//    }
    public List<String> getElectionPositions(int electionId) {
        List<String> positions = new ArrayList<>();
        EntityManager entityManager = emf.createEntityManager();

        try {
            Election election = entityManager.find(Election.class, electionId);
            if (election != null && election.getPositions() != null) {
                positions = Arrays.asList(election.getPositions().split(",")); // Assuming getPositions() returns a String
            }
        } catch (Exception e) {
            System.err.println("Error fetching positions for election ID " + electionId + ": " + e.getMessage());
        } finally {
            entityManager.close();
        }

        return positions;
    }

    @Override
    public void updateElection(Election election) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(election);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Rollback if there's an exception
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteElection(int id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Election election = entityManager.find(Election.class, id);
            if (election != null) {
                entityManager.remove(election);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Rollback if there's an exception
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    interface TransactionalOperation {

        void execute(EntityManager entityManager);
    }
}
