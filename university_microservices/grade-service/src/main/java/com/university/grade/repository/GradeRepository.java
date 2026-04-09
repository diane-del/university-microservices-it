package com.university.grade.repository;

import com.university.grade.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByCourseId(Long courseId);
    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    // Moyenne des notes pour un cours
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.courseId = :courseId")
    Double getAverageScoreByCourse(Long courseId);

    // Nombre d'admis pour un cours (score >= 18)
    @Query("SELECT COUNT(g) FROM Grade g WHERE g.courseId = :courseId AND g.passed = true")
    Long countPassedByCourse(Long courseId);
}
