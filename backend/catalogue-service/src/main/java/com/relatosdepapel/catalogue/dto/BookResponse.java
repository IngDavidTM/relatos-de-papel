package com.relatosdepapel.catalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String category;
    private String isbn;
    private BigDecimal rating;
    private Boolean visibility;
    private Integer stock;
    private BigDecimal price;
    private String description;
    private String coverUrl;
}
