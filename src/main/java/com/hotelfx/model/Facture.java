package com.hotelfx.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id", unique = true, nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private LocalDate date;

    public Facture(Reservation reservation, Double total, LocalDate date) {
        this.reservation = reservation;
        this.total = total;
        this.date = date;
    }
}
