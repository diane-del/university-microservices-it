package com.university.student.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "studenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    @Column(name = "nome", nullable = false)
    private String firstName;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Column(name = "cognome", nullable = false)
    private String lastName;

    @Email(message = "Email non valida")
    @NotBlank(message = "L'email è obbligatoria")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "data_nascita")
    private LocalDate dateOfBirth;

    @Column(name = "numero_telefono")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatoStudente status = StatoStudente.ATTIVO;

    public enum StatoStudente {
        ATTIVO, INATTIVO, SOSPESO
    }
}
