package com.relatosdepapel.catalogue.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Libro no encontrado con id: " + id);
    }
}
