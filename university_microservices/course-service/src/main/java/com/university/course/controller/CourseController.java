package com.university.course.controller;

import com.university.course.entity.Course;
import com.university.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/corsi")
@RequiredArgsConstructor
@Tag(name = "Corsi", description = "API per la gestione dei corsi universitari")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Recupera tutti i corsi")
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(summary = "Recupera i corsi disponibili")
    @GetMapping("/disponibili")
    public ResponseEntity<List<Course>> getAvailableCourses() {
        return ResponseEntity.ok(courseService.getAvailableCourses());
    }

    @Operation(summary = "Recupera un corso tramite ID")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(
            @Parameter(description = "ID del corso") @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Crea un nuovo corso")
    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course corso) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(corso));
    }

    @Operation(summary = "Modifica un corso esistente")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @Parameter(description = "ID del corso") @PathVariable Long id,
            @Valid @RequestBody Course dettagliCorso) {
        return ResponseEntity.ok(courseService.updateCourse(id, dettagliCorso));
    }

    @Operation(summary = "Elimina un corso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "ID del corso") @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verifica se un corso esiste (usato da Enrollment e Grade Service)")
    @GetMapping("/{id}/esiste")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID del corso") @PathVariable Long id) {
        return ResponseEntity.ok(courseService.existsById(id));
    }
}
