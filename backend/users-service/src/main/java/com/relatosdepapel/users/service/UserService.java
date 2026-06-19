package com.relatosdepapel.users.service;

import com.relatosdepapel.users.dto.RegisterRequest;
import com.relatosdepapel.users.dto.UserResponse;
import com.relatosdepapel.users.entity.User;
import com.relatosdepapel.users.exception.EmailAlreadyExistsException;
import com.relatosdepapel.users.exception.UserNotFoundException;
import com.relatosdepapel.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String DEFAULT_AVATAR =
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=400&q=80";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .avatar(DEFAULT_AVATAR)
                .bio("Nuevo lector de Relatos de Papel.")
                .role("ROLE_USER")
                .build();

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAvatar(),
                user.getBio(),
                user.getRole(),
                user.getCreatedAt());
    }
}
