package com.university.student.controller;

import com.university.student.entity.Student;
import com.university.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studenti")
@RequiredArgsConstructor
@Tag(name = "Studenti", description = "API per la gestione degli studenti")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Recupera tutti gli studenti")
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @Operation(summary = "Recupera uno studente tramite ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Studente trovato"),
        @ApiResponse(responseCode = "404", description = "Studente non trovato")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "ID dello studente") @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(summary = "Crea un nuovo studente")
    @ApiResponse(responseCode = "201", description = "Studente creato con successo")
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(student));
    }

    @Operation(summary = "Modifica uno studente esistente")
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @Parameter(description = "ID dello studente") @PathVariable Long id,
            @Valid @RequestBody Student dettagliStudente) {
        return ResponseEntity.ok(studentService.updateStudent(id, dettagliStudente));
    }

    @Operation(summary = "Elimina uno studente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "ID dello studente") @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verifica se uno studente esiste (usato da Enrollment e Grade Service)")
    @GetMapping("/{id}/esiste")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID dello studente") @PathVariable Long id) {
        return ResponseEntity.ok(studentService.existsById(id));
    }
}
