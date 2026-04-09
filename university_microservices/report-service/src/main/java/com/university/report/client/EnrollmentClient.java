package com.university.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "report-iscrizioni", url = "${servizi.iscrizioni.url}")
public interface EnrollmentClient {
    @GetMapping("/api/iscrizioni")
    List<Object> getAllEnrollments();

    @GetMapping("/api/iscrizioni/studente/{studentId}")
    List<Object> getEnrollmentsByStudent(@PathVariable("studentId") Long studentId);

    @GetMapping("/api/iscrizioni/corso/{courseId}")
    List<Object> getEnrollmentsByCourse(@PathVariable("courseId") Long courseId);
}
