package com.relatosdepapel.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;
import java.util.List;

/**
 * CORS centralizado en el Gateway. Permite que la SPA (servida desde otro
 * dominio, p. ej. Vercel) consuma el back-end. La autenticación viaja por la
 * cabecera Authorization (no cookies), por lo que no se requieren credenciales.
 *
 * Los orígenes permitidos se configuran con la variable de entorno
 * GATEWAY_CORS_ALLOWED_ORIGINS (lista separada por comas); por defecto "*".
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(
            @Value("${gateway.cors.allowed-origins:*}") String allowedOrigins) {

        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        config.setAllowedOriginPatterns(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("X-Auth-Error"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter((CorsConfigurationSource) source);
    }
}
