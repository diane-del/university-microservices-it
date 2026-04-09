package com.university.enrollment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI enrollmentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servizio Iscrizioni API")
                        .description("Gestione delle iscrizioni ai corsi — Comunica con Studenti e Corsi via Feign Client")
                        .version("1.0.0"));
    }
}
