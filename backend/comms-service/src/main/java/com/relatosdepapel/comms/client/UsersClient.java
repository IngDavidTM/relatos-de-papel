package com.relatosdepapel.comms.client;

import com.relatosdepapel.comms.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente para resolver los datos del cliente (email, nombre) a partir de su id,
 * usando el balanceo de Eureka (sin IP ni puerto).
 */
@FeignClient(name = "users-service")
public interface UsersClient {

    @GetMapping("/internal/users/{id}")
    UserResponse getById(@PathVariable("id") Long id);
}
