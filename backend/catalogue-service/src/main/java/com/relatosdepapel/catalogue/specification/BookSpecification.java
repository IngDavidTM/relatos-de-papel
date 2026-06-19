package com.relatosdepapel.catalogue.specification;

import com.relatosdepapel.catalogue.entity.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookSpecification {

    private BookSpecification() {}

    public static Specification<Book> withFilters(
            String title,
            String author,
            LocalDate publicationDate,
            String category,
            String isbn,
            BigDecimal minRating,
            Boolean visibility) {

        return Specification
                .where(hasTitle(title))
                .and(hasAuthor(author))
                .and(hasPublicationDate(publicationDate))
                .and(hasCategory(category))
                .and(hasIsbn(isbn))
                .and(hasMinRating(minRating))
                .and(isVisible(visibility));
    }

    private static Specification<Book> hasTitle(String title) {
        return (root, query, cb) -> StringUtils.hasText(title)
                ? cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%")
                : null;
    }

    private static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) -> StringUtils.hasText(author)
                ? cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%")
                : null;
    }

    private static Specification<Book> hasPublicationDate(LocalDate publicationDate) {
        return (root, query, cb) -> publicationDate != null
                ? cb.equal(root.get("publicationDate"), publicationDate)
                : null;
    }

    private static Specification<Book> hasCategory(String category) {
        return (root, query, cb) -> StringUtils.hasText(category)
                ? cb.like(cb.lower(root.get("category")), "%" + category.toLowerCase() + "%")
                : null;
    }

    private static Specification<Book> hasIsbn(String isbn) {
        return (root, query, cb) -> StringUtils.hasText(isbn)
                ? cb.equal(root.get("isbn"), isbn)
                : null;
    }

    private static Specification<Book> hasMinRating(BigDecimal minRating) {
        return (root, query, cb) -> minRating != null
                ? cb.greaterThanOrEqualTo(root.get("rating"), minRating)
                : null;
    }

    private static Specification<Book> isVisible(Boolean visibility) {
        return (root, query, cb) -> visibility != null
                ? cb.equal(root.get("visibility"), visibility)
                : null;
    }
}
