package com.relatosdepapel.users.dto;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        String avatar,
        String bio,
        String role,
        Instant createdAt
) {}
