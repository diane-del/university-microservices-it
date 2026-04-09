package com.university.enrollment.repository;

import com.university.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Tous les cours d'un étudiant
    List<Enrollment> findByStudentId(Long studentId);

    // Tous les étudiants inscrits à un cours
    List<Enrollment> findByCourseId(Long courseId);

    // Vérifier si un étudiant est déjà inscrit à un cours
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
