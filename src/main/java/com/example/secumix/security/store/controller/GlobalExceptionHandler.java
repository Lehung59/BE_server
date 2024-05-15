package com.example.secumix.security.store.controller;

import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseObject> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ResponseObject("FAILED", ex.getMessage(), ""));
    }
}