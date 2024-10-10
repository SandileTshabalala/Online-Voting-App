package com.voting.implementation;

import com.voting.dao.CandidateDao;
import com.voting.models.Candidate;
import com.voting.models.Election;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CandidateDaoImpl implements CandidateDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VotingSystemPU");

    private String generateUniqueCandidateId() {
        Random random = new Random();
        int candidateIdNumber = 10000 + random.nextInt(90000);
        return "CAND-" + candidateIdNumber;
    }

    @Override
    public void registerCandidate(Candidate candidate, int electionId) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            // Find the election by its ID
            Election election = entityManager.find(Election.class, electionId);

            if (election == null) {
                throw new RuntimeException("Election with ID " + electionId + " not found.");
            }

            // Begin transaction
            entityManager.getTransaction().begin();

            // Assign the Election to the Candidate
            candidate.setElection(election);

            // If the candidate doesn't have an ID, generate a unique ID
            if (candidate.getCandidateId() == null) {
                candidate.setCandidateId(generateUniqueCandidateId());
            }

            // Persist the candidate
            entityManager.persist(candidate);

            // Commit the transaction
            entityManager.getTransaction().commit();

            System.out.println("Candidate registered successfully.");
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error registering candidate", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Candidate> getAllCandidates() {
        EntityManager entityManager = emf.createEntityManager();
        List<Candidate> candidates = entityManager.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList();
        entityManager.close();
        return candidates;
    }

    @Override
    public Candidate getCandidateById(int id) {
        EntityManager entityManager = emf.createEntityManager();
        Candidate candidate = entityManager.find(Candidate.class, id);
        entityManager.close();
        return candidate;
    }

    @Override
    public List<Candidate> getCandidatesForElectionAndPosition(int electionId, String position) {
        List<Candidate> candidates = new ArrayList<>();
        EntityManager entityManager = emf.createEntityManager();

        try {
            TypedQuery<Candidate> query = entityManager.createQuery(
                    "SELECT c FROM Candidate c WHERE c.election.id = :electionId AND c.position = :position AND c.status = :status", Candidate.class);
            query.setParameter("electionId", electionId);
            query.setParameter("position", position);
            query.setParameter("status", "Approved");
            candidates = query.getResultList();
        } finally {
            entityManager.close();
        }

        return candidates;
    }

    @Override
    public List<Candidate> getCandidatesForElection(int electionId) {
        EntityManager entityManager = emf.createEntityManager();
        List<Candidate> candidates = new ArrayList<>();

        try {
            candidates = entityManager.createQuery(
                    "SELECT c FROM Candidate c WHERE c.election.id = :electionId AND c.status = :status", Candidate.class)
                    .setParameter("electionId", electionId)
                    .setParameter("status", "Approved") 
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return candidates;
    }

    @Override
    public void updateCandidateStatus(int id, String status) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Candidate candidate = entityManager.find(Candidate.class,
                id);
        if (candidate != null) {
            candidate.setStatus(status);
            entityManager.merge(candidate);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

//        @Override
//    public int getTotalCandidateCount() {
//        EntityManager entityManager = emf.createEntityManager();
//        Query query = entityManager.createQuery("SELECT COUNT(c) FROM Candidate c");
//        return ((Long) query.getSingleResult()).intValue();
//    }
    @Override
    public void approveCandidate(int id) {
        updateCandidateStatus(id, "Approved");
    }

    @Override
    public void rejectCandidate(int id) {
        updateCandidateStatus(id, "Rejected");
    }
}
