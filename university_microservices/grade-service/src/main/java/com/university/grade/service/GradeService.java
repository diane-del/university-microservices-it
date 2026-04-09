package com.university.grade.service;

import com.university.grade.client.CourseClient;
import com.university.grade.client.StudentClient;
import com.university.grade.entity.Grade;
import com.university.grade.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    // ─── Assegna un voto ───────────────────────────────────────────────────────
    public Grade assignGrade(Long studenteId, Long corsoId, Integer voto,
                             Boolean conLode, String commenti) {

        // 1. Verifica che lo studente esista → chiama il Servizio Studenti
        Boolean studenteEsiste = studentClient.existsById(studenteId);
        if (studenteEsiste == null || !studenteEsiste) {
            throw new RuntimeException("Studente non trovato con id : " + studenteId);
        }

        // 2. Verifica che il corso esista → chiama il Servizio Corsi
        Boolean corsoEsiste = courseClient.existsById(corsoId);
        if (corsoEsiste == null || !corsoEsiste) {
            throw new RuntimeException("Corso non trovato con id : " + corsoId);
        }

        // 3. Verifica che il voto non esista già
        if (gradeRepository.existsByStudentIdAndCourseId(studenteId, corsoId)) {
            throw new RuntimeException(
                "Un voto esiste già per lo studente " + studenteId +
                " nel corso " + corsoId + ". Utilizzare la modifica."
            );
        }

        // 4. La lode è possibile solo con 30
        if (voto < 30) conLode = false;

        Grade votoEntity = Grade.builder()
                .studentId(studenteId)
                .courseId(corsoId)
                .score(voto)
                .cumLaude(conLode != null && conLode)
                .comments(commenti)
                .passed(voto >= 18)
                .build();

        return gradeRepository.save(votoEntity);
    }

    // ─── Modifica un voto ─────────────────────────────────────────────────────
    public Grade updateGrade(Long id, Integer voto, Boolean conLode, String commenti) {
        Grade votoEntity = getGradeById(id);
        votoEntity.setScore(voto);
        votoEntity.setCumLaude(voto == 30 && conLode != null && conLode);
        votoEntity.setComments(commenti);
        votoEntity.setPassed(voto >= 18);
        return gradeRepository.save(votoEntity);
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voto non trovato con id : " + id));
    }

    public List<Grade> getGradesByStudent(Long studenteId) {
        return gradeRepository.findByStudentId(studenteId);
    }

    public List<Grade> getGradesByCourse(Long corsoId) {
        return gradeRepository.findByCourseId(corsoId);
    }

    public Grade getGradeByStudentAndCourse(Long studenteId, Long corsoId) {
        return gradeRepository.findByStudentIdAndCourseId(studenteId, corsoId)
                .orElseThrow(() -> new RuntimeException(
                    "Nessun voto per lo studente " + studenteId + " nel corso " + corsoId));
    }

    public Double getAverageByCourse(Long corsoId) {
        return gradeRepository.getAverageScoreByCourse(corsoId);
    }

    public void deleteGrade(Long id) {
        gradeRepository.delete(getGradeById(id));
    }
}
