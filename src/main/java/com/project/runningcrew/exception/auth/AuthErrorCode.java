package com.project.runningcrew.exception.auth;

import com.project.runningcrew.exception.common.BaseErrorCode;

public enum AuthErrorCode implements BaseErrorCode {

    AUTHENTICATION("AUTH_01", "인증이 필요합니다."),
    AUTHORIZATION("AUTH_02", "권한이 없습니다.");

    private String code;
    private String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
