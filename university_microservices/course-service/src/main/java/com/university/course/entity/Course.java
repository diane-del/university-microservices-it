package com.university.course.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "corsi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome del corso è obbligatorio")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Il professore è obbligatorio")
    @Column(nullable = false)
    private String professor;

    @NotNull(message = "I crediti sono obbligatori")
    @Positive(message = "I crediti devono essere positivi")
    @Column(nullable = false)
    private Integer credits;

    @Column(name = "max_studenti")
    @Builder.Default
    private Integer maxStudents = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatoCorso status = StatoCorso.DISPONIBILE;

    public enum StatoCorso {
        DISPONIBILE, PIENO, ANNULLATO
    }
}
