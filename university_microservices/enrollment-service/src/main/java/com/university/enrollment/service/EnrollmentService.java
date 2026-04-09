package com.university.enrollment.service;

import com.university.enrollment.client.CourseClient;
import com.university.enrollment.client.StudentClient;
import com.university.enrollment.entity.Enrollment;
import com.university.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    // Feign Clients — comunicazione tra i microservizi
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    // ─── Iscrivere uno studente a un corso ─────────────────────────────────────
    public Enrollment enrollStudent(Long studenteId, Long corsoId) {

        // 1. Verifica che lo studente esista → chiamata HTTP al Servizio Studenti
        Boolean studenteEsiste = studentClient.existsById(studenteId);
        if (studenteEsiste == null || !studenteEsiste) {
            throw new RuntimeException("Studente non trovato con id : " + studenteId);
        }

        // 2. Verifica che il corso esista → chiamata HTTP al Servizio Corsi
        Boolean corsoEsiste = courseClient.existsById(corsoId);
        if (corsoEsiste == null || !corsoEsiste) {
            throw new RuntimeException("Corso non trovato con id : " + corsoId);
        }

        // 3. Verifica che lo studente non sia già iscritto a questo corso
        if (enrollmentRepository.existsByStudentIdAndCourseId(studenteId, corsoId)) {
            throw new RuntimeException(
                "Lo studente " + studenteId + " è già iscritto al corso " + corsoId
            );
        }

        // 4. Tutto OK → salva l'iscrizione
        Enrollment iscrizione = Enrollment.builder()
                .studentId(studenteId)
                .courseId(corsoId)
                .build();

        return enrollmentRepository.save(iscrizione);
    }

    // ─── Recupera tutte le iscrizioni ──────────────────────────────────────────
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // ─── Recupera un'iscrizione tramite ID ─────────────────────────────────────
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Iscrizione non trovata con id : " + id));
    }

    // ─── Recupera tutti i corsi di uno studente ─────────────────────────────────
    public List<Enrollment> getEnrollmentsByStudent(Long studenteId) {
        Boolean studenteEsiste = studentClient.existsById(studenteId);
        if (studenteEsiste == null || !studenteEsiste) {
            throw new RuntimeException("Studente non trovato con id : " + studenteId);
        }
        return enrollmentRepository.findByStudentId(studenteId);
    }

    // ─── Recupera tutti gli studenti di un corso ─────────────────────────────────
    public List<Enrollment> getEnrollmentsByCourse(Long corsoId) {
        Boolean corsoEsiste = courseClient.existsById(corsoId);
        if (corsoEsiste == null || !corsoEsiste) {
            throw new RuntimeException("Corso non trovato con id : " + corsoId);
        }
        return enrollmentRepository.findByCourseId(corsoId);
    }

    // ─── Annulla un'iscrizione ──────────────────────────────────────────────────
    public Enrollment cancelEnrollment(Long id) {
        Enrollment iscrizione = getEnrollmentById(id);
        iscrizione.setStatus(Enrollment.StatoIscrizione.ANNULLATA);
        return enrollmentRepository.save(iscrizione);
    }

    // ─── Elimina un'iscrizione ──────────────────────────────────────────────────
    public void deleteEnrollment(Long id) {
        Enrollment iscrizione = getEnrollmentById(id);
        enrollmentRepository.delete(iscrizione);
    }
}
