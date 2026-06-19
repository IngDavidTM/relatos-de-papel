package com.relatosdepapel.orders.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de dominio publicado cuando un pedido se registra correctamente.
 * Lo consume el microservicio comms para notificar por email al cliente.
 */
public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        BigDecimal totalAmount,
        int itemCount,
        LocalDateTime createdAt
) {}
