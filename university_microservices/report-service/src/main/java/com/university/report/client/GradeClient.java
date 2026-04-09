package com.university.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "report-voti", url = "${servizi.voti.url}")
public interface GradeClient {
    @GetMapping("/api/voti")
    List<Object> getAllGrades();

    @GetMapping("/api/voti/studente/{studentId}")
    List<Object> getGradesByStudent(@PathVariable("studentId") Long studentId);

    @GetMapping("/api/voti/corso/{courseId}/media")
    Map<String, Object> getAverageByCourse(@PathVariable("courseId") Long courseId);
}
