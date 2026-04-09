package com.university.enrollment.controller;

import com.university.enrollment.entity.Enrollment;
import com.university.enrollment.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iscrizioni")
@RequiredArgsConstructor
@Tag(name = "Iscrizioni", description = "API per le iscrizioni ai corsi — chiama Studenti e Corsi via Feign")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(
        summary = "Iscrivere uno studente a un corso",
        description = "Verifica tramite Feign che studente e corso esistano prima di iscrivere"
    )
    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(
            @Parameter(description = "ID dello studente") @RequestParam Long studentId,
            @Parameter(description = "ID del corso") @RequestParam Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollStudent(studentId, courseId));
    }

    @Operation(summary = "Recupera tutte le iscrizioni")
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @Operation(summary = "Recupera un'iscrizione tramite ID")
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @Operation(summary = "Visualizza tutti i corsi di uno studente")
    @GetMapping("/studente/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @Operation(summary = "Visualizza tutti gli studenti di un corso")
    @GetMapping("/corso/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @Operation(summary = "Annulla un'iscrizione")
    @PatchMapping("/{id}/annulla")
    public ResponseEntity<Enrollment> cancelEnrollment(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.cancelEnrollment(id));
    }

    @Operation(summary = "Elimina un'iscrizione")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
