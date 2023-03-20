package com.project.runningcrew.exception;

public class PasswordCheckFailException extends RuntimeException {
    public PasswordCheckFailException() {
        super("비밀번호가 이전 값과 일치하지 않습니다.");
    }
}
