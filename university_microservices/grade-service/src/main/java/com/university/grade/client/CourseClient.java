package com.university.grade.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feign-corsi", url = "${servizi.corsi.url}")
public interface CourseClient {
    @GetMapping("/api/corsi/{id}/esiste")
    Boolean existsById(@PathVariable("id") Long id);
}
