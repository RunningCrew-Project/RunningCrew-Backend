package com.project.runningcrew.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("권한이 없습니다.");
    }
}
