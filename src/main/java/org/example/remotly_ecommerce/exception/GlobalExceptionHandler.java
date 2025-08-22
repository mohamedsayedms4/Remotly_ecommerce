package org.example.remotly_ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .reduce((m1, m2) -> m1 + "; " + m2)
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorDetails(request, HttpStatus.BAD_REQUEST,
                        errorMessage));
    }

    // validation errors from manual validator
    // validation errors from manual validator (@RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        // اجمع كل الرسائل اللي جاية من الأنوتيشنز (@NotBlank, @Email, @Pattern ...)
        String errorMessage = ex.getConstraintViolations().stream()
                .map(cv -> cv.getMessage()) // هنا بيجيب الرسالة من ملف messages.properties
                .reduce((m1, m2) -> m1 + "; " + m2) // لو فيه أكتر من violation
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorDetails(request, HttpStatus.BAD_REQUEST, errorMessage));
    }


    // known business exceptions
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            InvalidEmail.class,
            InvalidPWD.class,
            InvalidOtp.class,
            InvalidPhoneNumber.class
    })
    public ResponseEntity<ErrorDetails> handleConflictExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        log.warn("Conflict Exception: {}", ex.getMessage());


        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorDetails(request, HttpStatus.CONFLICT, ex.getMessage()));
    }

    // NOT_FOUND exceptions
    @ExceptionHandler({
            SellerException.class,
            ProductException.class,
            CategoryException.class,
            UserException.class
    })
    public ResponseEntity<ErrorDetails> handleNotFoundExceptions(
            Exception ex,
            HttpServletRequest request) {

        log.warn("Not Found Exception: {}", ex.getMessage());


        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorDetails(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // Bad Request exceptions
    @ExceptionHandler(ProductCreationException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestExceptions(
            Exception ex,
            HttpServletRequest request) {

        log.warn("Bad Request Exception: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorDetails(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorDetails(request, HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal server error", "An unexpected error occurred"));
    }


    // unified error response - النسخة الأساسية
    private ErrorDetails buildErrorDetails(HttpServletRequest request,
                                           HttpStatus status,
                                           String message) {
        return buildErrorDetails(request, status, message, message);
    }

    // unified error response - النسخة مع التفاصيل
    private ErrorDetails buildErrorDetails(HttpServletRequest request,
                                           HttpStatus status,
                                           String message,
                                           String details) {
        ErrorDetails errorResponse = new ErrorDetails();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(message);
        errorResponse.setPath(request.getRequestURI());
        return errorResponse;
    }
}