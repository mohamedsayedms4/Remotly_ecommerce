package org.example.remotly_ecommerce.exception;

public class InvalidOtp extends RuntimeException {
    public InvalidOtp(String message) {
        super(message);
    }
}
