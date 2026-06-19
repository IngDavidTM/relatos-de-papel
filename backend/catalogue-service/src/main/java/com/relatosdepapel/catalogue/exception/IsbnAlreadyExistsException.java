package com.relatosdepapel.catalogue.exception;

public class IsbnAlreadyExistsException extends RuntimeException {

    public IsbnAlreadyExistsException(String isbn) {
        super("Ya existe un libro con el ISBN: " + isbn);
    }
}
