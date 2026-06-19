package com.relatosdepapel.gateway.security;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * Define qué endpoints son públicos y cuáles están protegidos.
 *
 * Regla general: cualquier endpoint que opere con datos de un cliente es
 * protegido. El resto (autenticación, registro y navegación del catálogo de
 * solo lectura) es público.
 */
@Component
public class RouteSecurityRules {

    public boolean isPublic(HttpMethod method, String path) {
        // Infraestructura y documentación
        if (path.startsWith("/actuator")
                || path.startsWith("/api-docs")
                || path.contains("/swagger-ui")
                || path.contains("/v3/api-docs")) {
            return true;
        }

        // Chat de soporte (WebSocket): acceso público.
        if (path.startsWith("/ws/")) {
            return true;
        }

        // Autenticación: crear, validar, renovar y revocar tokens
        if (path.startsWith("/api/auth/tokens")) {
            return true;
        }

        // Registro de nuevos usuarios: POST /api/users (exacto).
        // El resto de /api/users/** (perfil) es protegido.
        if (HttpMethod.POST.equals(method) && path.equals("/api/users")) {
            return true;
        }

        // Navegación del catálogo (solo lectura) es pública.
        // Las escrituras sobre el catálogo (POST/PUT/PATCH/DELETE) son protegidas.
        if (HttpMethod.GET.equals(method) && path.startsWith("/api/catalogue")) {
            return true;
        }

        return false;
    }
}
