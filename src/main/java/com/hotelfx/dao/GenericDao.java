package com.hotelfx.dao;

import com.hotelfx.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class GenericDao<T> {

    private final Class<T> entityClass;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        executeInsideTransaction(em -> em.persist(entity));
    }

    public void update(T entity) {
        executeInsideTransaction(em -> em.merge(entity));
    }

    public void delete(T entity) {
        executeInsideTransaction(em -> em.remove(em.contains(entity) ? entity : em.merge(entity)));
    }

    public Optional<T> findById(Long id) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(em.find(entityClass, id));
        }
    }

    public List<T> findAll() {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
        }
    }

    protected void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
