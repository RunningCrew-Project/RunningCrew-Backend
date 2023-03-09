package com.project.runningcrew.exception;

public class JwtVerificationException extends RuntimeException {
    public JwtVerificationException(String message) {
        super(message);
    }
}
