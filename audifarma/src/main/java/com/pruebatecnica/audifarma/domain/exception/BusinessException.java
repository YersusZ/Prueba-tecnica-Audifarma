package com.pruebatecnica.audifarma.domain.exception;

/**
 * Thrown when a domain business rule is violated.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
