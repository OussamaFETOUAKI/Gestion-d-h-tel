package com.hotelfx.dao;

import com.hotelfx.model.Client;
import com.hotelfx.util.JpaUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClientDao extends GenericDao<Client> {

    public ClientDao() {
        super(Client.class);
    }

    public List<Client> search(String keyword) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Client c WHERE c.nom LIKE :kw OR c.email LIKE :kw OR c.telephone LIKE :kw", Client.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        }
    }
}
