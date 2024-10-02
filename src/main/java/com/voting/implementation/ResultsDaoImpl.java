package com.voting.implementation;

import com.voting.models.Results;
import com.voting.dao.ResultsDao;
import com.voting.models.Votes;
import jakarta.persistence.*;
import java.util.List;

public class ResultsDaoImpl implements ResultsDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VotingSystemPU");
    @Override
    public Results calculateElectionResults(int electionId) {
        EntityManager entityManager = emf.createEntityManager();
        Results results = new Results(electionId);

        try {
            // Fetch all votes for the election
            List<Votes> votesList = entityManager.createQuery("SELECT v FROM Votes v WHERE v.election.id = :electionId", Votes.class)
                    .setParameter("electionId", electionId)
                    .getResultList();

            // Count votes for each candidate
            for (Votes vote : votesList) {
                if (!vote.isAbstain() && vote.getCandidate() != null) {
                    results.addVote(vote.getCandidate().getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return results;
    }

}
//    @Override
//    public Results getResultsForElection(int electionId) {
//        Results results = new Results(electionId);
//
//        EntityManager em = emf.createEntityManager();
//        try {
//            // Use JPQL to retrieve the vote counts
//            String jpql = "SELECT v.candidateId, COUNT(v) AS voteCount "
//                    + "FROM Votes v WHERE v.electionId = :electionId "
//                    + "GROUP BY v.candidateId";
//            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
//            query.setParameter("electionId", electionId);
//
//            // Execute the query and process results
//            for (Object[] result : query.getResultList()) {
//                int candidateId = (Integer) result[0];
//                long voteCount = (Long) result[1];
//                results.addVote(candidateId, (int) voteCount); // Adds the candidate and their vote count
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            em.close(); // Ensure EntityManager is closed
//        }
//
//        return results;
//    }