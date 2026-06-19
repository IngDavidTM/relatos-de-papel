package com.relatosdepapel.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * WebClient.Builder con balanceo de carga del lado del cliente (Eureka),
     * de modo que el Gateway pueda invocar a users-service por su nombre
     * lógico (lb://users-service) sin IP ni puerto.
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
