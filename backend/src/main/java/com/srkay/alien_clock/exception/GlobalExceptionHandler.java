package com.srkay.alien_clock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> details = new HashMap<>();

        // Populate the details map with field-specific error messages
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            details.put(error.getField(), error.getDefaultMessage());
        });

        // Set the response structure
        response.put("message", "Validation failed");
        response.put("detail", details);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> details = new HashMap<>();

        // Extract the field name from the exception message (if applicable)
        String fieldName = extractFieldNameFromMessage(ex.getMessage());
        if (fieldName != null) {
            details.put(fieldName, ex.getMessage());
        }

        // Set the response structure
        response.put("message", ex.getMessage());
        response.put("detail", details);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Helper method to extract the field name from the exception message.
     * This assumes the message is in the format "FieldName is required".
     */
    private String extractFieldNameFromMessage(String message) {
        if (message != null && message.contains("is required")) {
            return message.split(" ")[0].toLowerCase(); // Extract the field name
        }
        return null;
    }
}