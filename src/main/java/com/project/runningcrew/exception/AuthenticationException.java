package com.project.runningcrew.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("인증이 필요합니다.");
    }
}
