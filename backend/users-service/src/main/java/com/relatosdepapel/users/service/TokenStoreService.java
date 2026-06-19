package com.relatosdepapel.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * Persistencia efímera (Redis) de la relación token opaco -> JWT.
 * Es el corazón del patrón phantom token: el cliente solo conoce el token
 * opaco; el JWT real se almacena aquí con un TTL alineado con su expiración.
 */
@Service
@RequiredArgsConstructor
public class TokenStoreService {

    private static final String KEY_PREFIX = "phantom:token:";

    private final StringRedisTemplate redisTemplate;

    /** Crea un token opaco nuevo y almacena su JWT asociado con TTL. */
    public String store(String jwt, long ttlSeconds) {
        String opaque = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(key(opaque), jwt, Duration.ofSeconds(ttlSeconds));
        return opaque;
    }

    /** Devuelve el JWT asociado a un token opaco, o null si no existe / expiró. */
    public String resolve(String opaque) {
        if (opaque == null || opaque.isBlank()) {
            return null;
        }
        return redisTemplate.opsForValue().get(key(opaque));
    }

    /** Revoca un token opaco (logout o renovación). */
    public void revoke(String opaque) {
        if (opaque != null && !opaque.isBlank()) {
            redisTemplate.delete(key(opaque));
        }
    }

    private String key(String opaque) {
        return KEY_PREFIX + opaque;
    }
}
