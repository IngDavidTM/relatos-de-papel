package com.relatosdepapel.orders.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CatalogueBookResponse {

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

    public CatalogueBookResponse() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public LocalDate getPublicationDate() { return publicationDate; }
    public String getCategory() { return category; }
    public String getIsbn() { return isbn; }
    public BigDecimal getRating() { return rating; }
    public Boolean getVisibility() { return visibility; }
    public Integer getStock() { return stock; }
    public BigDecimal getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCoverUrl() { return coverUrl; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublicationDate(LocalDate d) { this.publicationDate = d; }
    public void setCategory(String category) { this.category = category; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public void setVisibility(Boolean visibility) { this.visibility = visibility; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}
