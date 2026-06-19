package com.relatosdepapel.gateway.filter;

import java.util.Locale;
import java.util.Set;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class HttpMethodOverrideGlobalFilter implements GlobalFilter, Ordered {

    private static final String METHOD_OVERRIDE_HEADER = "X-HTTP-Method-Override";
    private static final Set<HttpMethod> ALLOWED_METHODS = Set.of(
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.PATCH,
            HttpMethod.DELETE
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (!HttpMethod.POST.equals(request.getMethod())) {
            return chain.filter(exchange);
        }

        String overrideMethod = request.getHeaders().getFirst(METHOD_OVERRIDE_HEADER);
        if (!StringUtils.hasText(overrideMethod)) {
            return chain.filter(exchange);
        }

        HttpMethod targetMethod;
        try {
            targetMethod = HttpMethod.valueOf(overrideMethod.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        if (!ALLOWED_METHODS.contains(targetMethod)) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        ServerHttpRequest mutatedRequest = request.mutate()
                .method(targetMethod)
                .headers(headers -> headers.remove(METHOD_OVERRIDE_HEADER))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
