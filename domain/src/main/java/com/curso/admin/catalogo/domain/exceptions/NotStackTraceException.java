package com.curso.admin.catalogo.domain.exceptions;

public class NotStackTraceException extends RuntimeException{

    public NotStackTraceException(final String message) {
        this(message, null);
    }

    public NotStackTraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
