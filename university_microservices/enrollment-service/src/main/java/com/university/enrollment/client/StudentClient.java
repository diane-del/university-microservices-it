package com.university.enrollment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign Client per chiamare il Servizio Studenti
@FeignClient(name = "servizio-studenti", url = "${servizi.studenti.url}")
public interface StudentClient {

    // Verifica se lo studente esiste
    @GetMapping("/api/studenti/{id}/esiste")
    Boolean existsById(@PathVariable("id") Long id);

    // Recupera i dati dello studente
    @GetMapping("/api/studenti/{id}")
    Object getStudentById(@PathVariable("id") Long id);
}
