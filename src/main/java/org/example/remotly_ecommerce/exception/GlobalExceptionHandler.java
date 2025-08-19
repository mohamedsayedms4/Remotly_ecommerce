package org.example.remotly_ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.utilis.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request,
            WebRequest webRequest) {

        // جمع كل رسائل الأخطاء
        String detailedMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorDetails(request, HttpStatus.BAD_REQUEST,
                        "Validation failed", detailedMessage));
    }

    // validation errors from manual validator
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request,
            WebRequest webRequest) {

        // جمع كل رسائل الأخطاء من التحقق اليدوي
        String detailedMessage = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorDetails(request, HttpStatus.BAD_REQUEST,
                        "Validation failed", detailedMessage));
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
            HttpServletRequest request,
            WebRequest webRequest) {

        log.warn("Conflict Exception: {}", ex.getMessage());

        // هنا يمكنك تخصيص التفاصيل حسب نوع الاستثناء
        String details = getConflictExceptionDetails(ex);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorDetails(request, HttpStatus.CONFLICT, ex.getMessage(), details));
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
            HttpServletRequest request,
            WebRequest webRequest) {

        log.warn("Not Found Exception: {}", ex.getMessage());

        String details = getNotFoundExceptionDetails(ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorDetails(request, HttpStatus.NOT_FOUND, ex.getMessage(), details));
    }

    // Bad Request exceptions
    @ExceptionHandler(ProductCreationException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestExceptions(
            Exception ex,
            HttpServletRequest request,
            WebRequest webRequest) {

        log.warn("Bad Request Exception: {}", ex.getMessage());
        String details = getBadRequestExceptionDetails(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorDetails(request, HttpStatus.BAD_REQUEST, ex.getMessage(), details));
    }

    // fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception ex,
            HttpServletRequest request,
            WebRequest webRequest) {

        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorDetails(request, HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal server error", "An unexpected error occurred"));
    }

    // Helper method لتحديد تفاصيل استثناءات الـ Conflict
    private String getConflictExceptionDetails(RuntimeException ex) {
        if (ex instanceof InvalidEmail) {
            return Message.InvalidEmail;
        } else if (ex instanceof InvalidPWD) {
            return Message.InvalidPWD;
        } else if (ex instanceof InvalidOtp) {
            return Message.InvalidOtp;
        } else if (ex instanceof UserAlreadyExistsException) {
            return Message.UserAlreadyExistsException;
        }else if (ex instanceof UserException) {
            return Message.UserException;
        }
        return ex.getMessage();
    }

    // Helper method لتحديد تفاصيل استثناءات الـ Not Found
    private String getNotFoundExceptionDetails(Exception ex) {
        if (ex instanceof SellerException) {
            return Message.SellerException;
        } else if (ex instanceof ProductException) {
            return Message.ProductException;
        } else if (ex instanceof CategoryException) {
            return Message.CategoryException;
        }
        return ex.getMessage();
    }

    private String getBadRequestExceptionDetails(Exception ex) {
        if (ex instanceof ProductCreationException) {
            return Message.ProductCreationException;
        }
        return ex.getMessage();
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
        errorResponse.setDetails(details);
        errorResponse.setPath(request.getRequestURI());
        return errorResponse;
    }
}