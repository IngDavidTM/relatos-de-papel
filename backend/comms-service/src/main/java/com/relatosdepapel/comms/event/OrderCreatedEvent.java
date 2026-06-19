package com.relatosdepapel.comms.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Réplica del contrato de evento publicado por orders-service.
 */
public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        BigDecimal totalAmount,
        int itemCount,
        LocalDateTime createdAt
) {}
