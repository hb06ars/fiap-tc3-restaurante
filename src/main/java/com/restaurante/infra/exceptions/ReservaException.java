package com.restaurante.infra.exceptions;

public class ReservaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReservaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservaException(String message) {
        super(message);
    }

}
