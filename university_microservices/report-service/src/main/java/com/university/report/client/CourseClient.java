package com.university.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "report-corsi", url = "${servizi.corsi.url}")
public interface CourseClient {
    @GetMapping("/api/corsi")
    List<Object> getAllCourses();
}
