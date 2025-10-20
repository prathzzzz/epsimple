package com.eps.module.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

public class ResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, HttpStatus status) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, "Request successful", HttpStatus.OK);
    }

    public static ResponseEntity<ApiResponse<Object>> error(String message, HttpStatus status) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ApiResponse<Object>> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ApiResponse<Object>> validationError(Map<String, String> errors) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("Validation failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
