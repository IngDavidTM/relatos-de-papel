package com.relatosdepapel.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequest {

    @Valid
    @NotEmpty
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {}

    public List<OrderItemRequest> getItems() { return items; }

    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
