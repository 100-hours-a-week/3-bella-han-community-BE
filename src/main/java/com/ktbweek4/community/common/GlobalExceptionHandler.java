package com.ktbweek4.community.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException e) {
        return ApiResponse.<Void>error(CommonCode.BAD_REQUEST, e.getMessage()).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleServer(Exception e) {
        return ApiResponse.<Void>error(CommonCode.INTERNAL_SERVER_ERROR).toResponseEntity();
    }
}