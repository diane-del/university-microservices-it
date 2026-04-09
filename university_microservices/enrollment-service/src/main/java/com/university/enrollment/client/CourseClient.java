package com.university.enrollment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign Client per chiamare il Servizio Corsi
@FeignClient(name = "servizio-corsi", url = "${servizi.corsi.url}")
public interface CourseClient {

    // Verifica se il corso esiste
    @GetMapping("/api/corsi/{id}/esiste")
    Boolean existsById(@PathVariable("id") Long id);

    // Recupera i dati del corso
    @GetMapping("/api/corsi/{id}")
    Object getCourseById(@PathVariable("id") Long id);
}
