package com.hotelfx.util;

import com.hotelfx.model.Chambre;
import com.hotelfx.model.Client;
import com.hotelfx.model.Reservation;
import com.hotelfx.service.HotelService;

import java.time.LocalDate;

public class DataSeeder {
    public static void main(String[] args) {
        HotelService service = new HotelService();

        System.out.println("Seeding data...");

        // Create Rooms
        if (service.getAllChambres().isEmpty()) {
            service.saveChambre(new Chambre("101", Chambre.TypeChambre.SIMPLE, 80.0));
            service.saveChambre(new Chambre("102", Chambre.TypeChambre.DOUBLE, 120.0));
            service.saveChambre(new Chambre("201", Chambre.TypeChambre.SUITE, 250.0));
            System.out.println("Rooms created.");
        } else {
            System.out.println("Rooms already exist.");
        }

        // Create Clients
        if (service.getAllClients().isEmpty()) {
            service.saveClient(new Client("Alice Dupont", "alice@example.com", "0601020304"));
            service.saveClient(new Client("Bob Martin", "bob@example.com", "0699887766"));
            System.out.println("Clients created.");
        } else {
            System.out.println("Clients already exist.");
        }

        System.out.println("Data seeding completed.");
        JpaUtil.shutdown();
    }
}
