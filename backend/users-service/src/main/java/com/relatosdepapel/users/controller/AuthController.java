package com.relatosdepapel.users.controller;

import com.relatosdepapel.users.dto.LoginRequest;
import com.relatosdepapel.users.dto.OpaqueTokenRequest;
import com.relatosdepapel.users.dto.TokenResponse;
import com.relatosdepapel.users.dto.ValidationResponse;
import com.relatosdepapel.users.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de autenticación (phantom token). El recurso gestionado es el
 * "token" opaco que referencia a un JWT almacenado en Redis.
 */
@RestController
@RequestMapping("/api/auth/tokens")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Autenticación mediante phantom token (JWT + token opaco)")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    @Operation(summary = "Crear token", description = "Valida credenciales y emite un token opaco (login)")
    public ResponseEntity<TokenResponse> createToken(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createToken(request));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validar token", description = "Introspección usada por el Gateway: devuelve el JWT asociado si el opaco es válido")
    public ResponseEntity<ValidationResponse> validateToken(@Valid @RequestBody OpaqueTokenRequest request) {
        return ResponseEntity.ok(authService.validateToken(request.token()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token", description = "Revoca el token opaco actual y emite uno nuevo")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody OpaqueTokenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.refreshToken(request.token()));
    }

    @PostMapping("/revoke")
    @Operation(summary = "Revocar token", description = "Invalida un token opaco (logout)")
    public ResponseEntity<Void> revokeToken(@Valid @RequestBody OpaqueTokenRequest request) {
        authService.revokeToken(request.token());
        return ResponseEntity.noContent().build();
    }
}
