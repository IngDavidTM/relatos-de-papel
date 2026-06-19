package com.relatosdepapel.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {

    @NotNull
    private Long userId;

    @Valid
    @NotEmpty
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {}

    public Long getUserId() { return userId; }
    public List<OrderItemRequest> getItems() { return items; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
