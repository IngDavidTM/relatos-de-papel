package com.relatosdepapel.catalogue.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI catalogueOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catalogue Service API")
                        .description("API RESTful del microservicio de catálogo de libros - Relatos de Papel")
                        .version("1.0.0"));
    }
}
