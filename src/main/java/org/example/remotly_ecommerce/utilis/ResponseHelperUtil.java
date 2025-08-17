package org.example.remotly_ecommerce.utilis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public class ResponseHelperUtil {

    /**
     * يبني ResponseEntity من Optional.
     * @param optionalObj الـOptional اللي محتوي على الكيان
     * @param notFoundMessage الرسالة لو مفيش كيان
     * @param <T> نوع الكيان
     * @return ResponseEntity جاهزة
     */
    public static <T> ResponseEntity<?> fromOptional(Optional<T> optionalObj, String notFoundMessage) {
        return optionalObj.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", notFoundMessage)));
    }


    /**
     * يبني ResponseEntity للخطأ مع رسالة مخصصة.
     */
    public static ResponseEntity<?> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }

    /**
     * يبني ResponseEntity للخطأ مع رسالة وتفاصيل إضافية.
     */
    public static ResponseEntity<?> error(HttpStatus status, String message, String details) {
        return ResponseEntity.status(status)
                .body(Map.of("error", message, "details", details));
    }
}
