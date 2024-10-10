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
