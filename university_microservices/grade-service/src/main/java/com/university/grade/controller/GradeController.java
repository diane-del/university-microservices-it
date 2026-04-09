package com.university.grade.controller;

import com.university.grade.entity.Grade;
import com.university.grade.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voti")
@RequiredArgsConstructor
@Tag(name = "Voti", description = "API per i voti — sistema italiano su 30, superato se score >= 18")
public class GradeController {

    private final GradeService gradeService;

    @Operation(
        summary = "Assegna un voto a uno studente",
        description = "Verifica via Feign che studente e corso esistano. Voto su 30, superato se >= 18. Lode solo se voto = 30."
    )
    @PostMapping
    public ResponseEntity<Grade> assignGrade(
            @Parameter(description = "ID dello studente") @RequestParam Long studentId,
            @Parameter(description = "ID del corso") @RequestParam Long courseId,
            @Parameter(description = "Voto su 30 (0-30)") @RequestParam Integer score,
            @Parameter(description = "30 e lode? (solo se voto = 30)") @RequestParam(required = false, defaultValue = "false") Boolean cumLaude,
            @Parameter(description = "Commento del professore") @RequestParam(required = false) String comments) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gradeService.assignGrade(studentId, courseId, score, cumLaude, comments));
    }

    @Operation(summary = "Recupera tutti i voti")
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @Operation(summary = "Recupera un voto tramite ID")
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(
            @Parameter(description = "ID del voto") @PathVariable Long id) {
        return ResponseEntity.ok(gradeService.getGradeById(id));
    }

    @Operation(summary = "Visualizza tutti i voti di uno studente")
    @GetMapping("/studente/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(
            @Parameter(description = "ID dello studente") @PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
    }

    @Operation(summary = "Visualizza tutti i voti di un corso")
    @GetMapping("/corso/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourse(
            @Parameter(description = "ID del corso") @PathVariable Long courseId) {
        return ResponseEntity.ok(gradeService.getGradesByCourse(courseId));
    }

    @Operation(summary = "Visualizza il voto di uno studente per un corso specifico")
    @GetMapping("/studente/{studentId}/corso/{courseId}")
    public ResponseEntity<Grade> getGradeByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(gradeService.getGradeByStudentAndCourse(studentId, courseId));
    }

    @Operation(summary = "Media dei voti di un corso")
    @GetMapping("/corso/{courseId}/media")
    public ResponseEntity<Map<String, Object>> getAverageByCourse(
            @Parameter(description = "ID del corso") @PathVariable Long courseId) {
        Double media = gradeService.getAverageByCourse(courseId);
        return ResponseEntity.ok(Map.of(
            "corsoId", courseId,
            "media", media != null ? media : 0.0,
            "votoMinimo", 18,
            "votoMassimo", 30
        ));
    }

    @Operation(summary = "Modifica un voto esistente")
    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(
            @PathVariable Long id,
            @Parameter(description = "Nuovo voto su 30") @RequestParam Integer score,
            @RequestParam(required = false, defaultValue = "false") Boolean cumLaude,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(gradeService.updateGrade(id, score, cumLaude, comments));
    }

    @Operation(summary = "Elimina un voto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(
            @Parameter(description = "ID del voto") @PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
