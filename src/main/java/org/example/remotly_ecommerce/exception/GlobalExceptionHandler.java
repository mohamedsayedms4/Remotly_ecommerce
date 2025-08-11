package org.example.remotly_ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(Map.of(
                        "timestamp", new Date(),
                        "status", HttpStatus.CONFLICT.value(),
                        "error", "Conflict",
                        "message", ex.getMessage()
                ));
    }
    @ExceptionHandler(InvalidEmail.class)
    public ResponseEntity<?> handleInvalidEmail(InvalidEmail ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(Map.of(
                        "timestamp", new Date(),
                        "status", HttpStatus.CONFLICT.value(),
                        "error", "Conflict",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidPWD.class)
    public ResponseEntity<?> handleInvalidPwd(InvalidPWD ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(Map.of(
                        "timestamp", new Date(),
                        "status", HttpStatus.CONFLICT.value(),
                        "error", "Conflict",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidOtp.class)
    public ResponseEntity<?> handleInvalidPwd(InvalidOtp ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(Map.of(
                        "timestamp", new Date(),
                        "status", HttpStatus.CONFLICT.value(),
                        "error", "Conflict",
                        "message", ex.getMessage()
                ));
    }

}
