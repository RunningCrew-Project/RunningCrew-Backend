package com.project.runningcrew.exception.jwt;

public class JwtVerificationException extends RuntimeException {
    public JwtVerificationException(String message) {
        super(message);
    }
}
