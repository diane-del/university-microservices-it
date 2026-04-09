package com.university.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "report-studenti", url = "${servizi.studenti.url}")
public interface StudentClient {
    @GetMapping("/api/studenti")
    List<Object> getAllStudents();
}
