package com.relatosdepapel.orders.dto;

import com.relatosdepapel.orders.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;

    public OrderResponse() {}

    public OrderResponse(Long id, Long userId, LocalDateTime createdAt, OrderStatus status,
                         BigDecimal totalAmount, List<OrderItemResponse> items) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<OrderItemResponse> getItems() { return items; }
}
