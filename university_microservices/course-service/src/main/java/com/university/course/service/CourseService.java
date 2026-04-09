package com.university.course.service;

import com.university.course.entity.Course;
import com.university.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corso non trovato con id : " + id));
    }

    public Course createCourse(Course corso) {
        return courseRepository.save(corso);
    }

    public Course updateCourse(Long id, Course dettagliCorso) {
        Course corso = getCourseById(id);
        corso.setName(dettagliCorso.getName());
        corso.setDescription(dettagliCorso.getDescription());
        corso.setProfessor(dettagliCorso.getProfessor());
        corso.setCredits(dettagliCorso.getCredits());
        corso.setMaxStudents(dettagliCorso.getMaxStudents());
        corso.setStatus(dettagliCorso.getStatus());
        return courseRepository.save(corso);
    }

    public void deleteCourse(Long id) {
        Course corso = getCourseById(id);
        courseRepository.delete(corso);
    }

    // Chiamato da Enrollment e Grade Service via Feign Client
    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findByStatus(Course.StatoCorso.DISPONIBILE);
    }
}
