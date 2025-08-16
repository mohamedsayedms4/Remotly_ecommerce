package org.example.remotly_ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle all known business exceptions (custom exceptions).
     */
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            InvalidEmail.class,
            InvalidPWD.class,
            InvalidOtp.class
    })
    public ResponseEntity<?> handleConflictExceptions(RuntimeException ex) {
        log.warn("Conflict Exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "timestamp", new Date(),
                "status", HttpStatus.CONFLICT.value(),
                "error", HttpStatus.CONFLICT.getReasonPhrase(),
                "message", ex.getMessage()
        ));
    }

    /**
     * Handle all NOT_FOUND type exceptions (Entity not found).
     */
    @ExceptionHandler({
            SellerException.class,
            ProductException.class,
            CategoryException.class,

    })
    public ResponseEntity<ErrorDetails> handleNotFoundExceptions(Exception ex,  // غير من RuntimeException إلى Exception
                                                                 HttpServletRequest request,
                                                                 WebRequest webRequest) {
        log.warn("Not Found Exception: {}", ex.getMessage());

        ErrorDetails errorResponse = new ErrorDetails();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(webRequest.getDescription(false));  // غير هذا أيضاً
        errorResponse.setDetails(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            ProductCreationException.class

    })
    public ResponseEntity<ErrorDetails> handleBadRequestExceptions(Exception ex,  // غير من RuntimeException إلى Exception
                                                                 HttpServletRequest request,
                                                                 WebRequest webRequest) {
        log.warn("Not Found Exception: {}", ex.getMessage());

        ErrorDetails errorResponse = new ErrorDetails();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(webRequest.getDescription(false));  // غير هذا أيضاً
        errorResponse.setDetails(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all fallback for unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", new Date(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "message", ex.getMessage(),
                "path", request.getRequestURI()
        ));
    }
}
