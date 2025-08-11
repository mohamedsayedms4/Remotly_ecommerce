package org.example.remotly_ecommerce.exception;

public class InvalidEmail extends RuntimeException {
    public InvalidEmail(String message) {
        super(message);
    }
}
