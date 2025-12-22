package com.hotelfx.dao;

import com.hotelfx.model.Reservation;
import com.hotelfx.model.Chambre;
import com.hotelfx.util.JpaUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class ReservationDao extends GenericDao<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }

    public List<Reservation> findByChambre(Chambre chambre) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            return em.createQuery("SELECT r FROM Reservation r WHERE r.chambre = :chambre", Reservation.class)
                    .setParameter("chambre", chambre)
                    .getResultList();
        }
    }

    public boolean isRoomAvailable(Chambre chambre, LocalDate start, LocalDate end) {
        // Overlap logic: (StartA < EndB) and (EndA > StartB)
        // Existing reservation R: R.start < end AND R.end > start
        try (EntityManager em = JpaUtil.getEntityManager()) {
            Long count = em.createQuery(
                    "SELECT COUNT(r) FROM Reservation r " +
                    "WHERE r.chambre = :chambre " +
                    "AND r.statut <> 'ANNULEE' " +
                    "AND r.dateDebut < :endDate " +
                    "AND r.dateFin > :startDate", Long.class)
                    .setParameter("chambre", chambre)
                    .setParameter("startDate", start)
                    .setParameter("endDate", end)
                    .getSingleResult();
            return count == 0;
        }
    }
}
