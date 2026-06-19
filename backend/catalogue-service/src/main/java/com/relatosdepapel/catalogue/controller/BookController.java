package com.relatosdepapel.catalogue.controller;

import com.relatosdepapel.catalogue.dto.BookPatchRequest;
import com.relatosdepapel.catalogue.dto.BookRequest;
import com.relatosdepapel.catalogue.dto.BookResponse;
import com.relatosdepapel.catalogue.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/catalogue/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Gestión del catálogo de libros")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Listar y buscar libros",
               description = "Devuelve todos los libros. Si se incluyen parámetros de búsqueda, filtra por ellos de forma combinada.")
    public ResponseEntity<Page<BookResponse>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publicationDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) Boolean visibility,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {

        boolean hasFilter = title != null || author != null || publicationDate != null
                || category != null || isbn != null || minRating != null || visibility != null;

        Page<BookResponse> result = hasFilter
                ? bookService.search(title, author, publicationDate, category, isbn, minRating, visibility, pageable)
                : bookService.findAll(pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID")
    public ResponseEntity<BookResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo libro")
    public ResponseEntity<BookResponse> create(
            @Valid @RequestBody BookRequest request,
            UriComponentsBuilder uriBuilder) {
        BookResponse created = bookService.create(request);
        URI location = uriBuilder
                .path("/api/catalogue/books/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualización total de un libro")
    public ResponseEntity<BookResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualización parcial de un libro")
    public ResponseEntity<BookResponse> partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody BookPatchRequest request) {
        return ResponseEntity.ok(bookService.partialUpdate(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
