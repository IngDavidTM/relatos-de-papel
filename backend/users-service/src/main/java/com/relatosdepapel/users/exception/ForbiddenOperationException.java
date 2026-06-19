package com.relatosdepapel.users.exception;

public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException() {
        super("No tienes permiso para consultar este perfil");
    }
}
