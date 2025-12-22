package com.hotelfx.service;

import com.hotelfx.dao.ChambreDao;
import com.hotelfx.dao.ClientDao;
import com.hotelfx.dao.FactureDao;
import com.hotelfx.dao.ReservationDao;
import com.hotelfx.model.Chambre;
import com.hotelfx.model.Client;
import com.hotelfx.model.Facture;
import com.hotelfx.model.Reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HotelService {

    private final ChambreDao chambreDao = new ChambreDao();
    private final ClientDao clientDao = new ClientDao();
    private final ReservationDao reservationDao = new ReservationDao();
    private final FactureDao factureDao = new FactureDao();

    // --- Rooms ---
    public List<Chambre> getAllChambres() {
        return chambreDao.findAll();
    }

    public void saveChambre(Chambre chambre) {
        if (chambre.getId() == null) {
            chambreDao.save(chambre);
        } else {
            chambreDao.update(chambre);
        }
    }

    public void deleteChambre(Chambre chambre) {
        chambreDao.delete(chambre);
    }

    // --- Clients ---
    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    public List<Client> searchClients(String keyword) {
        return clientDao.search(keyword);
    }

    public void saveClient(Client client) {
        if (client.getId() == null) {
            clientDao.save(client);
        } else {
            clientDao.update(client);
        }
    }

    public void deleteClient(Client client) {
        clientDao.delete(client);
    }

    // --- Reservations ---
    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }

    public Reservation createReservation(Chambre chambre, Client client, LocalDate start, LocalDate end) throws IllegalArgumentException {
        if (!reservationDao.isRoomAvailable(chambre, start, end)) {
            throw new IllegalArgumentException("Chambre non disponible pour cette période.");
        }
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new IllegalArgumentException("Dates invalides.");
        }

        Reservation reservation = new Reservation(chambre, client, start, end, Reservation.StatutReservation.CONFIRMEE);
        reservationDao.save(reservation);
        return reservation;
    }
    
    public void updateReservation(Reservation reservation) {
         // Re-check availability if dates changed? simplified for now
         reservationDao.update(reservation);
    }

    public void cancelReservation(Reservation reservation) {
        reservation.setStatut(Reservation.StatutReservation.ANNULEE);
        reservationDao.update(reservation);
    }

    // --- Billing ---
    public double calculateTotalAmount(Reservation reservation) {
        long nights = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        if (nights < 1) nights = 1; // Minimum 1 night billing or error
        return nights * reservation.getChambre().getTarif();
    }

    public Facture generateFacture(Reservation reservation) {
        double total = calculateTotalAmount(reservation);
        Facture facture = new Facture(reservation, total, LocalDate.now());
        factureDao.save(facture);
        
        reservation.setStatut(Reservation.StatutReservation.FACTUREE);
        reservationDao.update(reservation);
        
        return facture;
    }
}
