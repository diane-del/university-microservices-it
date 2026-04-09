package com.university.grade.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feign-studenti", url = "${servizi.studenti.url}")
public interface StudentClient {
    @GetMapping("/api/studenti/{id}/esiste")
    Boolean existsById(@PathVariable("id") Long id);
}
