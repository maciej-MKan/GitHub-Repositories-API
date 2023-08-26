package com.mkan.domain.exception;

public class AccessForbiddenException extends RuntimeException{

    public AccessForbiddenException(final String message) {
        super(message);
    }
}
