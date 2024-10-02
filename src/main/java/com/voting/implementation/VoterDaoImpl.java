/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voting.implementation;

/**
 *
 * @author USER
 */
import com.voting.dao.VoterDao;
import com.voting.models.Voter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.UUID;

public class VoterDaoImpl implements VoterDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("VotingSystemPU");

    @Override
    public void registerVoter(Voter voter) throws Exception {
        if (findVoterByEmail(voter.getEmail()) != null) {
            throw new Exception("Email already registered.");
        }

        // Encrypt the password using BCrypt
        voter.setPassword(BCrypt.hashpw(voter.getPassword(), BCrypt.gensalt()));

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(voter);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new Exception("Registration failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Voter authenticateVoter(String email, String password) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Voter> query = em.createQuery("SELECT v FROM Voter v WHERE v.email = :email", Voter.class);
            query.setParameter("email", email);
            Voter voter = query.getSingleResult();

            if (BCrypt.checkpw(password, voter.getPassword())) {
                return voter;
            } else {
                throw new Exception("Invalid password.");
            }
        } catch (NoResultException e) {
            throw new Exception("Authentication failed: Voter not found.", e);
        } catch (Exception e) {
            throw new Exception("Authentication failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Voter findVoterByEmail(String email) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Voter> query = em.createQuery("SELECT v FROM Voter v WHERE v.email = :email", Voter.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new Exception("Find by email failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Voter> getAllVoters() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Voter> query = em.createQuery("SELECT v FROM Voter v", Voter.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Voter findVoterByToken(String token) {
        EntityManager em = emf.createEntityManager();
        Voter voter = null;
        try {
            TypedQuery<Voter> query = em.createQuery("SELECT v FROM Voter v WHERE v.votingToken = :token", Voter.class);
            query.setParameter("token", token);
            voter = query.getSingleResult();
        } catch (NoResultException e) {
            System.out.print("No token found");
        } finally {
            em.close();
        }
        return voter;
    }

    @Override
    public void updateVoterStatus(int id, String status) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Voter voter = em.find(Voter.class, id);
            if (voter != null) {
                tx.begin();
                voter.setStatus(status);
                em.merge(voter);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteVoter(int id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Voter voter = entityManager.find(Voter.class, id);
            if (voter != null) {          
                entityManager.remove(voter);              
            }
            entityManager.getTransaction().commit();
        }  catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Rollback if there's an exception
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void generateVotingToken(Voter voter) throws Exception {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Fetch the latest voter entity from DB
            Voter managedVoter = em.find(Voter.class, voter.getId());
            if (managedVoter != null) {
                String token = UUID.randomUUID().toString();
                managedVoter.setVotingToken(token);
                managedVoter.setTokenUsed(false);
                em.merge(managedVoter);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new Exception("Failed to generate voting token: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void markTokenAsUsed(Voter voter) throws Exception {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Voter managedVoter = em.find(Voter.class, voter.getId());
            if (managedVoter != null) {
                managedVoter.setTokenUsed(true);
                em.merge(managedVoter);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new Exception("Failed to mark token as used: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Voter getVoterById(int id) throws Exception {
        EntityManager em = null;
        Voter voter = null;

        try {
            em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            TypedQuery<Voter> query = em.createQuery("SELECT v FROM Voter v WHERE v.id = :id", Voter.class);
            query.setParameter("id", id);
            voter = query.getSingleResult();
            tx.commit();
        } catch (NoResultException e) {
            throw new Exception("No voter found with ID: " + id, e);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new Exception("Error retrieving voter by ID: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return voter;
    }

    @Override
    public List<Voter> searchVoters(String queryStr) throws Exception {
        EntityManager em = emf.createEntityManager();
        List<Voter> voters = Collections.emptyList();
        try {
            TypedQuery<Voter> query = em.createQuery(
                    "SELECT v FROM Voter v WHERE LOWER(v.name) LIKE :query OR LOWER(v.email) LIKE :query",
                    Voter.class
            );
            query.setParameter("query", "%" + queryStr.toLowerCase() + "%");
            voters = query.getResultList();
        } catch (Exception e) {
            throw new Exception("Search failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
        return voters;
    }
    
       @FunctionalInterface
    interface TransactionalOperation {

        void execute(EntityManager entityManager);
    }
}
