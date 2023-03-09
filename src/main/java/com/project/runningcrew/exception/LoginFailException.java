package com.project.runningcrew.exception;

public class LoginFailException extends RuntimeException {
    public LoginFailException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
