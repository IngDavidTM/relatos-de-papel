package com.relatosdepapel.orders.dto;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private Long bookId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public OrderItemResponse() {}

    public OrderItemResponse(Long id, Long bookId, Integer quantity,
                             BigDecimal unitPrice, BigDecimal subtotal) {
        this.id = id;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public Long getId() { return id; }
    public Long getBookId() { return bookId; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
}
