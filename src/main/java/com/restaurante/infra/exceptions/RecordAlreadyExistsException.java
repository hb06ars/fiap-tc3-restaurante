package com.restaurante.infra.exceptions;

public class RecordAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

}
