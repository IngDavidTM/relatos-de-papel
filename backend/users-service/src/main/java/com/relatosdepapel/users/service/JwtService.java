package com.relatosdepapel.users.service;

import com.relatosdepapel.users.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Genera y parsea los JWT internos del patrón phantom token.
 * Estos JWT NUNCA se entregan al cliente: viajan únicamente del Gateway
 * hacia los microservicios destino como header "accessToken".
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationSeconds;
    private final String issuer;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds,
            @Value("${security.jwt.issuer:relatos-de-papel}") String issuer) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
        this.issuer = issuer;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key)
                .compact();
    }

    /**
     * Devuelve los claims si el JWT es válido y NO ha expirado; null en caso contrario.
     */
    public Claims parseValid(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (Exception ex) {
            return null;
        }
    }
}
