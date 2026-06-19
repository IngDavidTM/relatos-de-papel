package com.relatosdepapel.catalogue.service;

import com.relatosdepapel.catalogue.dto.BookPatchRequest;
import com.relatosdepapel.catalogue.dto.BookRequest;
import com.relatosdepapel.catalogue.dto.BookResponse;
import com.relatosdepapel.catalogue.entity.Book;
import com.relatosdepapel.catalogue.exception.BookNotFoundException;
import com.relatosdepapel.catalogue.exception.IsbnAlreadyExistsException;
import com.relatosdepapel.catalogue.repository.BookRepository;
import com.relatosdepapel.catalogue.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<BookResponse> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> search(
            String title, String author, LocalDate publicationDate,
            String category, String isbn, BigDecimal minRating,
            Boolean visibility, Pageable pageable) {
        Specification<Book> spec = BookSpecification.withFilters(
                title, author, publicationDate, category, isbn, minRating, visibility);
        return bookRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        if (request.getIsbn() != null && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new IsbnAlreadyExistsException(request.getIsbn());
        }
        return toResponse(bookRepository.save(fromRequest(request)));
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if (request.getIsbn() != null && bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new IsbnAlreadyExistsException(request.getIsbn());
        }
        Book book = fromRequest(request);
        book.setId(id);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse partialUpdate(Long id, BookPatchRequest request) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if (request.getIsbn() != null && bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new IsbnAlreadyExistsException(request.getIsbn());
        }
        applyPatch(book, request);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publicationDate(book.getPublicationDate())
                .category(book.getCategory())
                .isbn(book.getIsbn())
                .rating(book.getRating())
                .visibility(book.getVisibility())
                .stock(book.getStock())
                .price(book.getPrice())
                .description(book.getDescription())
                .coverUrl(book.getCoverUrl())
                .build();
    }

    private Book fromRequest(BookRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publicationDate(request.getPublicationDate())
                .category(request.getCategory())
                .isbn(request.getIsbn())
                .rating(request.getRating())
                .visibility(request.getVisibility() != null ? request.getVisibility() : true)
                .stock(request.getStock() != null ? request.getStock() : 0)
                .price(request.getPrice())
                .description(request.getDescription())
                .coverUrl(request.getCoverUrl())
                .build();
    }

    private void applyPatch(Book book, BookPatchRequest request) {
        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getPublicationDate() != null) book.setPublicationDate(request.getPublicationDate());
        if (request.getCategory() != null) book.setCategory(request.getCategory());
        if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getRating() != null) book.setRating(request.getRating());
        if (request.getVisibility() != null) book.setVisibility(request.getVisibility());
        if (request.getStock() != null) book.setStock(request.getStock());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getDescription() != null) book.setDescription(request.getDescription());
        if (request.getCoverUrl() != null) book.setCoverUrl(request.getCoverUrl());
    }
}
