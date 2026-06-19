package com.relatosdepapel.gateway.client;

import com.relatosdepapel.gateway.dto.ValidationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Cliente reactivo que el Gateway usa para validar tokens opacos contra
 * users-service (a través de Eureka, sin IP ni puerto).
 */
@Component
public class AuthClient {

    private static final String USERS_VALIDATE_URL = "lb://users-service/api/auth/tokens/validate";

    private final WebClient webClient;

    public AuthClient(WebClient.Builder loadBalancedWebClientBuilder) {
        this.webClient = loadBalancedWebClientBuilder.build();
    }

    public Mono<ValidationResponse> validate(String opaqueToken) {
        return webClient.post()
                .uri(USERS_VALIDATE_URL)
                .bodyValue(Map.of("token", opaqueToken))
                .retrieve()
                .bodyToMono(ValidationResponse.class)
                .onErrorResume(ex -> Mono.just(new ValidationResponse(false, null, null, null, null)));
    }
}
