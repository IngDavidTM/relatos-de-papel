package com.relatosdepapel.gateway.dto;

/**
 * Respuesta de introspección que devuelve users-service al validar un token opaco.
 */
public record ValidationResponse(
        boolean valid,
        String jwt,
        Long userId,
        String email,
        String role
) {}
