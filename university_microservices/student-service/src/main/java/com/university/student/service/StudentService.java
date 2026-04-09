package com.university.student.service;

import com.university.student.entity.Student;
import com.university.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    // Recupera tutti gli studenti
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Recupera uno studente tramite ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Studente non trovato con id : " + id));
    }

    // Crea un nuovo studente
    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Uno studente con questa email esiste già : " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    // Modifica uno studente esistente
    public Student updateStudent(Long id, Student dettagliStudente) {
        Student student = getStudentById(id);
        student.setFirstName(dettagliStudente.getFirstName());
        student.setLastName(dettagliStudente.getLastName());
        student.setEmail(dettagliStudente.getEmail());
        student.setDateOfBirth(dettagliStudente.getDateOfBirth());
        student.setPhoneNumber(dettagliStudente.getPhoneNumber());
        student.setStatus(dettagliStudente.getStatus());
        return studentRepository.save(student);
    }

    // Elimina uno studente
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    // Verifica se uno studente esiste (chiamato da Enrollment Service via Feign)
    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }
}
