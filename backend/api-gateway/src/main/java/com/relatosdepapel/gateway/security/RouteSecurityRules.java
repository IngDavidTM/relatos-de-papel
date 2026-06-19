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

    private static final String TOKEN_BASE_PATH = "/api/auth/tokens";

    public boolean isInternalOnly(String path) {
        return path.equals(TOKEN_BASE_PATH + "/validate");
    }

    public boolean requiresAdmin(HttpMethod method, String path) {
        return path.startsWith("/api/catalogue") && !HttpMethod.GET.equals(method);
    }

    public boolean isPublic(HttpMethod method, String path) {
        // Las solicitudes CORS preflight no contienen credenciales.
        if (HttpMethod.OPTIONS.equals(method)) {
            return true;
        }

        // Solo se exponen las sondas necesarias para la plataforma.
        if (HttpMethod.GET.equals(method)
                && (path.equals("/actuator/health") || path.equals("/actuator/info"))) {
            return true;
        }

        // Chat de soporte (WebSocket): acceso público.
        if (path.startsWith("/ws/")) {
            return true;
        }

        // La introspección /validate es exclusivamente interna (Gateway -> users).
        if (HttpMethod.POST.equals(method)
                && (path.equals(TOKEN_BASE_PATH)
                || path.equals(TOKEN_BASE_PATH + "/refresh")
                || path.equals(TOKEN_BASE_PATH + "/revoke"))) {
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
