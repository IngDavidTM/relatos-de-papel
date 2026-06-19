package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.entity.Order;
import com.relatosdepapel.orders.entity.OrderStatus;
import com.relatosdepapel.orders.event.OrderEventPublisher;
import com.relatosdepapel.orders.exception.OrderNotFoundException;
import com.relatosdepapel.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTests {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(
                orderRepository,
                mock(OrderItemValidationService.class),
                mock(OrderEventPublisher.class));
    }

    @Test
    void returnsOrderOnlyForItsOwner() {
        Order order = new Order();
        order.setId(10L);
        order.setUserId(7L);
        order.setStatus(OrderStatus.COMPLETED);
        order.setTotalAmount(new BigDecimal("25.00"));
        when(orderRepository.findByIdAndUserId(10L, 7L)).thenReturn(Optional.of(order));

        var response = orderService.findByIdForUser(10L, 7L);

        assertEquals(10L, response.getId());
        assertEquals(7L, response.getUserId());
        verify(orderRepository).findByIdAndUserId(10L, 7L);
    }

    @Test
    void hidesOrdersBelongingToAnotherUser() {
        when(orderRepository.findByIdAndUserId(10L, 8L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.findByIdForUser(10L, 8L));
    }
}
