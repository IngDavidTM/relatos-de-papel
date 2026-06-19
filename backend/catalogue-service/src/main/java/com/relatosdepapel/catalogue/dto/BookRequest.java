package com.relatosdepapel.catalogue.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    private LocalDate publicationDate;

    private String category;

    private String isbn;

    @DecimalMin(value = "1.0", message = "La valoración mínima es 1.0")
    @DecimalMax(value = "5.0", message = "La valoración máxima es 5.0")
    private BigDecimal rating;

    private Boolean visibility = true;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock = 0;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    private BigDecimal price;

    private String description;

    private String coverUrl;
}
