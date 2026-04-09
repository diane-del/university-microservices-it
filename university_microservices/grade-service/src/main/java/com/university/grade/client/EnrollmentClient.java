package com.university.grade.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "feign-iscrizioni", url = "${servizi.iscrizioni.url}")
public interface EnrollmentClient {
    // Verifica che lo studente sia iscritto al corso prima di assegnare un voto
    @GetMapping("/api/iscrizioni/studente/{studentId}")
    List<Object> getEnrollmentsByStudent(@PathVariable("studentId") Long studentId);
}
