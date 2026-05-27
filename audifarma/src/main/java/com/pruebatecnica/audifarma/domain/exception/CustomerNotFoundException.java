package com.pruebatecnica.audifarma.domain.exception;

import java.util.UUID;

/**
 * Thrown when a customer lookup returns no result.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(UUID id) {
        super("Customer not found with id: " + id);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
