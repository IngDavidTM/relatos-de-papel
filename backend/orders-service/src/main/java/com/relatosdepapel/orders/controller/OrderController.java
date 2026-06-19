package com.relatosdepapel.orders.controller;

import com.relatosdepapel.orders.dto.CreateOrderRequest;
import com.relatosdepapel.orders.dto.OrderResponse;
import com.relatosdepapel.orders.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @RequestHeader("X-User-Id") @Positive Long authenticatedUserId,
            @Valid @RequestBody CreateOrderRequest request,
            UriComponentsBuilder uriBuilder) {
        OrderResponse created = orderService.create(authenticatedUserId, request);
        URI location = uriBuilder
                .path("/api/orders/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(
            @RequestHeader("X-User-Id") @Positive Long authenticatedUserId,
            @PathVariable @Positive Long id) {
        return ResponseEntity.ok(orderService.findByIdForUser(id, authenticatedUserId));
    }

    @GetMapping("/me/recent")
    public ResponseEntity<List<OrderResponse>> findRecentForCurrentUser(
            @RequestHeader("X-User-Id") @Positive Long authenticatedUserId,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit) {
        return ResponseEntity.ok(orderService.findRecentByUserId(authenticatedUserId, limit));
    }
}
