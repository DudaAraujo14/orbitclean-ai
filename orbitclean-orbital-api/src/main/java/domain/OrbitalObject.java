package br.com.orbitclean.orbital.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ORBITAL_OBJECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrbitalObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "norad_id", nullable = false, unique = true)
    private Integer noradId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "object_type", nullable = false, length = 50)
    private String objectType;

    @Column(name = "operational_status", nullable = false, length = 50)
    private String operationalStatus;

    @Column(name = "mass_kg")
    private Double massKg;

    @Column(name = "altitude_km")
    private Double altitudeKm;

    @Column(name = "inclination_deg")
    private Double inclinationDeg;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "circular_value_score")
    private Integer circularValueScore;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.riskScore == null) {
            this.riskScore = 0;
        }

        if (this.circularValueScore == null) {
            this.circularValueScore = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}