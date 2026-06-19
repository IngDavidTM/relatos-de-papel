package com.relatosdepapel.users.dto;

/**
 * Respuesta al cliente tras crear o renovar un token.
 * Solo se entrega el token OPACO; el JWT nunca sale del back-end.
 */
public record TokenResponse(
        String accessToken,   // token opaco (referencia)
        String tokenType,     // "Bearer"
        long expiresIn        // segundos de validez
) {}
