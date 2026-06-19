package com.relatosdepapel.gateway.filter;

import com.relatosdepapel.gateway.client.AuthClient;
import com.relatosdepapel.gateway.security.RouteSecurityRules;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Defensor perimetral de la arquitectura (patrón phantom token).
 *
 * Para los endpoints protegidos:
 *   1. Extrae el token OPACO de la cabecera Authorization: Bearer &lt;opaco&gt;.
 *   2. Lo valida contra users-service.
 *   3. Si es válido, inyecta el JWT asociado como cabecera "accessToken" en la
 *      petición reenviada al microservicio destino.
 *   4. Si falta o es inválido, responde 401 sin alcanzar al microservicio.
 *
 * Para los endpoints públicos, la petición se reenvía sin tocar.
 *
 * Se ejecuta después de {@link HttpMethodOverrideGlobalFilter} para conocer el
 * método HTTP real ya transcrito.
 */
@Component
public class AuthenticationGlobalFilter implements GlobalFilter, Ordered {

    public static final String ACCESS_TOKEN_HEADER = "accessToken";
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthClient authClient;
    private final RouteSecurityRules securityRules;

    public AuthenticationGlobalFilter(AuthClient authClient, RouteSecurityRules securityRules) {
        this.authClient = authClient;
        this.securityRules = securityRules;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if (securityRules.isInternalOnly(path)) {
            return forbidden(exchange, "Endpoint disponible solo para comunicaciones internas");
        }

        if (securityRules.isPublic(request.getMethod(), path)) {
            return chain.filter(exchange);
        }

        String opaqueToken = extractToken(request);
        if (!StringUtils.hasText(opaqueToken)) {
            return unauthorized(exchange, "Falta el token de autenticación");
        }

        return authClient.validate(opaqueToken)
                .flatMap(validation -> {
                    if (!validation.valid()) {
                        return unauthorized(exchange, "Token inválido o expirado");
                    }

                    if (securityRules.requiresAdmin(request.getMethod(), path)
                            && !"ROLE_ADMIN".equals(validation.role())) {
                        return forbidden(exchange, "Se requiere el rol de administrador");
                    }

                    ServerHttpRequest mutatedRequest = request.mutate()
                            .headers(headers -> {
                                // Se reemplazan posibles valores enviados por el cliente.
                                headers.set(ACCESS_TOKEN_HEADER, validation.jwt());
                                headers.set("X-User-Id", String.valueOf(validation.userId()));
                                headers.set("X-User-Email", validation.email());
                                headers.set("X-User-Role", validation.role());
                            })
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("X-Auth-Error", message);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().add("X-Auth-Error", message);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // Después del filtro de transcripción de método (HIGHEST_PRECEDENCE),
        // para que el método HTTP real ya esté resuelto.
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
