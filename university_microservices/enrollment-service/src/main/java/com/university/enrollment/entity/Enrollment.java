package com.university.enrollment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "iscrizioni",
    uniqueConstraints = {
        // Uno studente non può iscriversi due volte allo stesso corso
        @UniqueConstraint(columnNames = {"studente_id", "corso_id"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Salviamo solo l'ID — i dati vengono dal Student Service
    @NotNull(message = "L'ID dello studente è obbligatorio")
    @Column(name = "studente_id", nullable = false)
    private Long studentId;

    // Salviamo solo l'ID — i dati vengono dal Course Service
    @NotNull(message = "L'ID del corso è obbligatorio")
    @Column(name = "corso_id", nullable = false)
    private Long courseId;

    @Column(name = "data_iscrizione", nullable = false)
    @Builder.Default
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatoIscrizione status = StatoIscrizione.ATTIVA;

    public enum StatoIscrizione {
        ATTIVA, ANNULLATA, COMPLETATA
    }
}
