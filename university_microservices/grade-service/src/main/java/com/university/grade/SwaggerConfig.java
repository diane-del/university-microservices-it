package com.university.grade;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI gradeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servizio Voti API")
                        .description("Gestione dei voti — Sistema italiano su 30 | Superato se score >= 18 | 30 e lode possibile")
                        .version("1.0.0"));
    }
}
