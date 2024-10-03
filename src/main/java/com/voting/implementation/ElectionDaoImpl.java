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

    @Override
    public void updateElectionStatus(int id, String status) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Election election = em.find(Election.class, id);
            if (election != null) {
                election.setStatus(status);
                em.merge(election);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error updating election status", e);
        } finally {
            em.close();
        }
    }
    //dashboard
//    @Override
//    public int getTotalElectionCount() {
//        EntityManager entityManager = emf.createEntityManager();
//        Query query = entityManager.createQuery("SELECT COUNT(e) FROM Election e");
//        return ((Long) query.getSingleResult()).intValue();
//    }
//
//    @Override
//    public int getActiveElectionCount() {
//        EntityManager entityManager = emf.createEntityManager();
//        Query query = entityManager.createQuery("SELECT COUNT(e) FROM Election e WHERE e.status = active");
//        return ((Long) query.getSingleResult()).intValue();
//    }
//
//    @Override
//    public int getTotalVoteCount() {
//        EntityManager entityManager = emf.createEntityManager();
//        Query query = entityManager.createQuery("SELECT COUNT(v) FROM Vote v");
//        return ((Long) query.getSingleResult()).intValue();
//    }

    @FunctionalInterface
    interface TransactionalOperation {

        void execute(EntityManager entityManager);
    }
}
