package com.relatosdepapel.comms.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CataloguePage(List<BookSummary> content) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookSummary(
            String title,
            String author,
            String category,
            BigDecimal price,
            BigDecimal rating
    ) {}
}
