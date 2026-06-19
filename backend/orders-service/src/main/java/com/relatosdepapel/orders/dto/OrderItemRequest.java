package com.relatosdepapel.orders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequest {

    @NotNull
    private Long bookId;

    @NotNull
    @Positive
    private Integer quantity;

    public OrderItemRequest() {}

    public Long getBookId() { return bookId; }
    public Integer getQuantity() { return quantity; }

    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
