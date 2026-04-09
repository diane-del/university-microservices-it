package com.university.grade.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "voti",
    uniqueConstraints = {
        // Uno studente può avere un solo voto per corso
        @UniqueConstraint(columnNames = {"studente_id", "corso_id"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID dello studente è obbligatorio")
    @Column(name = "studente_id", nullable = false)
    private Long studentId;

    @NotNull(message = "L'ID del corso è obbligatorio")
    @Column(name = "corso_id", nullable = false)
    private Long courseId;

    // Sistema italiano: voto tra 0 e 30
    // Superato se voto >= 18
    @NotNull(message = "Il voto è obbligatorio")
    @Min(value = 0, message = "Il voto minimo è 0")
    @Max(value = 30, message = "Il voto massimo è 30")
    @Column(nullable = false)
    private Integer score;

    // Menzione speciale: 30 e lode
    @Column(name = "con_lode")
    @Builder.Default
    private Boolean cumLaude = false;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "data_voto")
    @Builder.Default
    private LocalDateTime gradedAt = LocalDateTime.now();

    // Calcolato automaticamente: superato se score >= 18
    @Column(nullable = false)
    @Builder.Default
    private Boolean passed = false;

    // Chiamato prima del salvataggio per calcolare passed e lode
    @PrePersist
    @PreUpdate
    public void calcolaStato() {
        this.passed = this.score != null && this.score >= 18;
        // La lode è possibile solo con 30
        if (this.score != null && this.score < 30) {
            this.cumLaude = false;
        }
    }
}
