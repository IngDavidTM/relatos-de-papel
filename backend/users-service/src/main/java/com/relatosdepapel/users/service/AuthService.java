package com.relatosdepapel.users.service;

import com.relatosdepapel.users.dto.LoginRequest;
import com.relatosdepapel.users.dto.TokenResponse;
import com.relatosdepapel.users.dto.ValidationResponse;
import com.relatosdepapel.users.entity.User;
import com.relatosdepapel.users.exception.InvalidCredentialsException;
import com.relatosdepapel.users.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación basado en el patrón phantom token.
 *
 * Flujo:
 *  - createToken: valida credenciales, genera JWT, lo guarda en Redis bajo un
 *    token opaco y devuelve SOLO el opaco al cliente.
 *  - validateToken: el Gateway envía el opaco; se resuelve el JWT y, si sigue
 *    siendo válido, se devuelve para inyectarlo como header accessToken.
 *  - refreshToken: revoca el opaco actual y emite un par nuevo (JWT + opaco).
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenStoreService tokenStore;

    /** Operación: CREAR TOKEN (login). */
    public TokenResponse createToken(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return issueFor(user);
    }

    /** Operación: VALIDAR TOKEN (introspección que usa el Gateway). */
    public ValidationResponse validateToken(String opaqueToken) {
        String jwt = tokenStore.resolve(opaqueToken);
        if (jwt == null) {
            return ValidationResponse.invalid();
        }

        Claims claims = jwtService.parseValid(jwt);
        if (claims == null) {
            // JWT expirado o manipulado: limpiamos el opaco asociado.
            tokenStore.revoke(opaqueToken);
            return ValidationResponse.invalid();
        }

        return new ValidationResponse(
                true,
                jwt,
                Long.valueOf(claims.getSubject()),
                claims.get("email", String.class),
                claims.get("role", String.class));
    }

    /** Operación: RENOVAR TOKEN. */
    public TokenResponse refreshToken(String opaqueToken) {
        String jwt = tokenStore.resolve(opaqueToken);
        if (jwt == null) {
            throw new InvalidCredentialsException();
        }
        Claims claims = jwtService.parseValid(jwt);
        if (claims == null) {
            tokenStore.revoke(opaqueToken);
            throw new InvalidCredentialsException();
        }

        Long userId = Long.valueOf(claims.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);

        tokenStore.revoke(opaqueToken);
        return issueFor(user);
    }

    /** Operación auxiliar: REVOCAR TOKEN (logout). */
    public void revokeToken(String opaqueToken) {
        tokenStore.revoke(opaqueToken);
    }

    private TokenResponse issueFor(User user) {
        String jwt = jwtService.generateToken(user);
        String opaque = tokenStore.store(jwt, jwtService.getExpirationSeconds());
        return new TokenResponse(opaque, "Bearer", jwtService.getExpirationSeconds());
    }
}
