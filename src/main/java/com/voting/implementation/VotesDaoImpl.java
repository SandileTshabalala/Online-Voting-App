package com.voting.implementation;

import com.voting.models.Votes;

import java.util.List;
import com.voting.dao.VotesDao;
import jakarta.persistence.*;

public class VotesDaoImpl implements VotesDao {
    
   private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VotingSystemPU");
 
     @Override
    public void saveVote(Votes vote) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(vote);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Votes> getVotesForElection(int electionId) {
        EntityManager em = emf.createEntityManager();
        List<Votes> votes = null;
        try {
            TypedQuery<Votes> query = em.createQuery(
                "SELECT v FROM Votes v WHERE v.election.id = :electionId", Votes.class);
            query.setParameter("electionId", electionId);
            votes = query.getResultList();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return votes;
    }

    @Override
    public boolean hasVoted(int electionId, int voterId) {
        EntityManager em = emf.createEntityManager();
        boolean hasVoted = false;
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(v) FROM Votes v WHERE v.election.id = :electionId AND v.voter.id = :voterId", Long.class);
            query.setParameter("electionId", electionId);
            query.setParameter("voterId", voterId);
            hasVoted = query.getSingleResult() > 0;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return hasVoted;
    }
    
    @Override
    public Votes findVoteByVotingTokenElectionPosition(String votingToken, int electionId, String position) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Votes> query = em.createQuery(
                "SELECT v FROM Votes v WHERE v.votingToken = :votingToken AND v.election.id = :electionId AND v.position = :position", 
                Votes.class);
            query.setParameter("votingToken", votingToken);
            query.setParameter("electionId", electionId);
            query.setParameter("position", position);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}


