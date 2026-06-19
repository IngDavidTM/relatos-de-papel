package com.relatosdepapel.users.controller;

import com.relatosdepapel.users.dto.RegisterRequest;
import com.relatosdepapel.users.dto.UserResponse;
import com.relatosdepapel.users.exception.ForbiddenOperationException;
import com.relatosdepapel.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gestión de usuarios y perfiles")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request,
            UriComponentsBuilder uriBuilder) {
        UserResponse created = userService.register(request);
        URI location = uriBuilder
                .path("/api/users/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener el perfil del usuario autenticado",
               description = "Resuelve el usuario a partir de la cabecera X-User-Id que inyecta el Gateway tras validar el token")
    public ResponseEntity<UserResponse> getCurrent(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener el perfil de un usuario")
    public ResponseEntity<UserResponse> getById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long authenticatedUserId,
            @RequestHeader("X-User-Role") String role) {
        if (!id.equals(authenticatedUserId) && !"ROLE_ADMIN".equals(role)) {
            throw new ForbiddenOperationException();
        }
        return ResponseEntity.ok(userService.findById(id));
    }
}
