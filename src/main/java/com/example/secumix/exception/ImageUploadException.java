package com.example.secumix.exception;

import org.springframework.http.HttpStatus;

public class ImageUploadException extends Exception {
    private final HttpStatus status;

    public ImageUploadException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}