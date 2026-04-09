package com.university.report;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI reportServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servizio Rapporti API")
                        .description("Rapporti e statistiche — Aggrega i dati di Studenti, Corsi, Iscrizioni e Voti")
                        .version("1.0.0"));
    }
}
