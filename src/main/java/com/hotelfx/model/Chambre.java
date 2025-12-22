package com.hotelfx.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chambres")
@Data
@NoArgsConstructor
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeChambre type;

    @Column(nullable = false)
    private Double tarif;

    public enum TypeChambre {
        SIMPLE, DOUBLE, SUITE
    }

    public Chambre(String numero, TypeChambre type, Double tarif) {
        this.numero = numero;
        this.type = type;
        this.tarif = tarif;
    }
}
