package com.relatosdepapel.users.dto;

/**
 * Resultado de la introspección de un token opaco.
 * Si es válido, se devuelve el JWT asociado (no expirado) que el Gateway
 * inyectará como header accessToken hacia el microservicio destino.
 */
public record ValidationResponse(
        boolean valid,
        String jwt,
        Long userId,
        String email,
        String role
) {
    public static ValidationResponse invalid() {
        return new ValidationResponse(false, null, null, null, null);
    }
}
