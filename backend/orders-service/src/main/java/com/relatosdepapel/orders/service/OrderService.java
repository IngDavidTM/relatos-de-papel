package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.client.dto.CatalogueBookResponse;
import com.relatosdepapel.orders.dto.CreateOrderRequest;
import com.relatosdepapel.orders.dto.OrderItemRequest;
import com.relatosdepapel.orders.dto.OrderItemResponse;
import com.relatosdepapel.orders.dto.OrderResponse;
import com.relatosdepapel.orders.entity.Order;
import com.relatosdepapel.orders.entity.OrderItem;
import com.relatosdepapel.orders.entity.OrderStatus;
import com.relatosdepapel.orders.event.OrderCreatedEvent;
import com.relatosdepapel.orders.event.OrderEventPublisher;
import com.relatosdepapel.orders.exception.OrderNotFoundException;
import com.relatosdepapel.orders.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemValidationService orderItemValidationService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository,
                        OrderItemValidationService orderItemValidationService,
                        OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderItemValidationService = orderItemValidationService;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public OrderResponse create(Long authenticatedUserId, CreateOrderRequest request) {
        Map<Long, CatalogueBookResponse> booksById = orderItemValidationService.validateItems(request.getItems());

        Order order = new Order();
        order.setUserId(authenticatedUserId);
        order.setStatus(OrderStatus.COMPLETED);
        order.setTotalAmount(BigDecimal.ZERO);

        request.getItems().stream()
                .map(item -> toEntity(item, booksById.get(item.getBookId())))
                .forEach(order::addItem);

        order.setTotalAmount(calculateTotal(order.getItems()));

        Order saved = orderRepository.save(order);

        orderEventPublisher.publishOrderCreated(new OrderCreatedEvent(
                saved.getId(),
                saved.getUserId(),
                saved.getTotalAmount(),
                saved.getItems().size(),
                saved.getCreatedAt()));

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse findByIdForUser(Long id, Long authenticatedUserId) {
        return orderRepository.findByIdAndUserId(id, authenticatedUserId)
                .map(this::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findRecentByUserId(Long userId, int limit) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, limit)).stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderItem toEntity(OrderItemRequest request, CatalogueBookResponse book) {
        OrderItem item = new OrderItem();
        item.setBookId(request.getBookId());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(book.getPrice());
        return item;
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getItems().stream().map(this::toItemResponse).toList()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new OrderItemResponse(
                item.getId(),
                item.getBookId(),
                item.getQuantity(),
                item.getUnitPrice(),
                subtotal
        );
    }
}
