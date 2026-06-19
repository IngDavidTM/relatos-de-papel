package com.relatosdepapel.orders.service;

import com.relatosdepapel.orders.client.CatalogueClient;
import com.relatosdepapel.orders.client.dto.CatalogueBookResponse;
import com.relatosdepapel.orders.dto.OrderItemRequest;
import com.relatosdepapel.orders.exception.InvalidOrderItemException;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderItemValidationService {

    private final CatalogueClient catalogueClient;

    public OrderItemValidationService(CatalogueClient catalogueClient) {
        this.catalogueClient = catalogueClient;
    }

    public Map<Long, CatalogueBookResponse> validateItems(List<OrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderItemException("Order must contain at least one item");
        }

        items.forEach(this::validateItemRequest);

        Map<Long, Integer> requestedQuantities = items.stream()
                .collect(Collectors.groupingBy(
                        OrderItemRequest::getBookId,
                        LinkedHashMap::new,
                        Collectors.summingInt(OrderItemRequest::getQuantity)));

        Map<Long, CatalogueBookResponse> booksById = new LinkedHashMap<>();
        requestedQuantities.forEach((bookId, requestedQuantity) -> {
            CatalogueBookResponse book = findBook(bookId);
            validateBook(book, bookId, requestedQuantity);
            booksById.put(bookId, book);
        });

        return booksById;
    }

    private void validateItemRequest(OrderItemRequest item) {
        if (item == null) {
            throw new InvalidOrderItemException("Order item is required");
        }
        if (item.getBookId() == null) {
            throw new InvalidOrderItemException("Book id is required");
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new InvalidOrderItemException("Quantity must be greater than zero for book id " + item.getBookId());
        }
    }

    private void validateBook(CatalogueBookResponse book, Long bookId, Integer requestedQuantity) {
        if (!Boolean.TRUE.equals(book.getVisibility())) {
            throw new InvalidOrderItemException("Book id " + bookId + " is hidden in catalogue");
        }
        if (book.getStock() == null || book.getStock() < requestedQuantity) {
            throw new InvalidOrderItemException("Insufficient stock for book id " + bookId);
        }
        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderItemException("Invalid catalogue price for book id " + bookId);
        }
    }

    private CatalogueBookResponse findBook(Long bookId) {
        try {
            return catalogueClient.findBookById(bookId);
        } catch (FeignException.NotFound exception) {
            throw new InvalidOrderItemException("Book id " + bookId + " does not exist");
        }
    }
}
