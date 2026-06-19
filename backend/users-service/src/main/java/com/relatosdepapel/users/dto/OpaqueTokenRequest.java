package com.relatosdepapel.users.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Cuerpo usado por el Gateway para validar o renovar un token opaco.
 */
public record OpaqueTokenRequest(
        @NotBlank(message = "El token es obligatorio")
        String token
) {}
