package com.university.student;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI studentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servizio Studenti API")
                        .description("Gestione degli studenti — Microservizio universitario")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("University Microservices")
                                .email("admin@universita.it")));
    }
}
