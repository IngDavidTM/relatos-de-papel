package com.relatosdepapel.users.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usersOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users & Auth Service API")
                        .description("Microservicio de usuarios y autenticación mediante phantom token (JWT + token opaco) - Relatos de Papel")
                        .version("1.0.0"));
    }
}
