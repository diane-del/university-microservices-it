package com.university.report.controller;

import com.university.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rapporti")
@RequiredArgsConstructor
@Tag(name = "Rapporti", description = "API per i rapporti — aggrega i dati di tutti i microservizi")
public class ReportController {

    private final ReportService reportService;

    @Operation(
        summary = "Rapporto globale dell'università",
        description = "Chiama Studenti, Corsi, Iscrizioni e Voti via Feign per aggregare tutte le statistiche"
    )
    @GetMapping("/globale")
    public ResponseEntity<Map<String, Object>> getGlobalReport() {
        return ResponseEntity.ok(reportService.getGlobalReport());
    }

    @Operation(
        summary = "Rapporto completo di uno studente",
        description = "Restituisce tutte le iscrizioni e i voti di uno studente"
    )
    @GetMapping("/studente/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentReport(
            @Parameter(description = "ID dello studente") @PathVariable Long studentId) {
        return ResponseEntity.ok(reportService.getStudentReport(studentId));
    }

    @Operation(
        summary = "Rapporto completo di un corso",
        description = "Restituisce tutti gli studenti iscritti e la media dei voti per un corso"
    )
    @GetMapping("/corso/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseReport(
            @Parameter(description = "ID del corso") @PathVariable Long courseId) {
        return ResponseEntity.ok(reportService.getCourseReport(courseId));
    }
}
