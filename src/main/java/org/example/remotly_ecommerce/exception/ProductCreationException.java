package org.example.remotly_ecommerce.exception;

public class ProductCreationException extends RuntimeException {
    public ProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
