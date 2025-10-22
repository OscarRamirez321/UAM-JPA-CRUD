package edu.uam.persistence.services;

import edu.uam.persistence.util.JPAConexion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MyDao<T, ID> implements ICRUD<T, ID> {

    private final Class<T> clazz;

    public MyDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T create(T entity) {
        return persistInTx(entityManager -> entityManager.persist(entity), entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            T entity = em.find(clazz, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = JPAConexion.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error updating " + clazz.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(ID id) {
        EntityManager em = JPAConexion.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T ref = em.find(clazz, id);
            if (ref != null) {
                em.remove(ref);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error deleting " + clazz.getSimpleName() + " id=" + id, e);
        } finally {
            em.close();
        }
    }

    private T persistInTx(Consumer<EntityManager> work, T entity) {
        EntityManager em = JPAConexion.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            work.accept(em);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error persisting " + clazz.getSimpleName(), e);
        } finally {
            em.close();
        }
    }
}
