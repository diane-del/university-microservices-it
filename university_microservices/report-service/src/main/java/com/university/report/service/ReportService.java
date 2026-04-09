package com.university.report.service;

import com.university.report.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final StudentClient studentClient;
    private final CourseClient courseClient;
    private final EnrollmentClient enrollmentClient;
    private final GradeClient gradeClient;

    // ─── Rapporto globale dell'università ────────────────────────────────────
    public Map<String, Object> getGlobalReport() {
        List<Object> studenti    = studentClient.getAllStudents();
        List<Object> corsi       = courseClient.getAllCourses();
        List<Object> iscrizioni  = enrollmentClient.getAllEnrollments();
        List<Object> voti        = gradeClient.getAllGrades();

        Map<String, Object> rapporto = new HashMap<>();
        rapporto.put("totaleStudenti",    studenti.size());
        rapporto.put("totaleCorsi",       corsi.size());
        rapporto.put("totaleIscrizioni",  iscrizioni.size());
        rapporto.put("totaleVoti",        voti.size());
        rapporto.put("tipoRapporto",      "GLOBALE");

        return rapporto;
    }

    // ─── Rapporto per studente ────────────────────────────────────────────────
    public Map<String, Object> getStudentReport(Long studenteId) {
        List<Object> iscrizioni = enrollmentClient.getEnrollmentsByStudent(studenteId);
        List<Object> voti       = gradeClient.getGradesByStudent(studenteId);

        Map<String, Object> rapporto = new HashMap<>();
        rapporto.put("studenteId",        studenteId);
        rapporto.put("totaleIscrizioni",  iscrizioni.size());
        rapporto.put("totaleVoti",        voti.size());
        rapporto.put("iscrizioni",        iscrizioni);
        rapporto.put("voti",              voti);
        rapporto.put("tipoRapporto",      "STUDENTE");

        return rapporto;
    }

    // ─── Rapporto per corso ───────────────────────────────────────────────────
    public Map<String, Object> getCourseReport(Long corsoId) {
        List<Object> iscrizioni      = enrollmentClient.getEnrollmentsByCourse(corsoId);
        Map<String, Object> mediaRis = gradeClient.getAverageByCourse(corsoId);

        Map<String, Object> rapporto = new HashMap<>();
        rapporto.put("corsoId",           corsoId);
        rapporto.put("totaleIscrizioni",  iscrizioni.size());
        rapporto.put("mediaVoti",         mediaRis.get("media"));
        rapporto.put("votoMinimo",        18);
        rapporto.put("votoMassimo",       30);
        rapporto.put("iscrizioni",        iscrizioni);
        rapporto.put("tipoRapporto",      "CORSO");

        return rapporto;
    }
}
