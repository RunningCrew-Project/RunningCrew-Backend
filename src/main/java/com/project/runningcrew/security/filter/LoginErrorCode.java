package com.project.runningcrew.security.filter;

import com.project.runningcrew.exception.common.BaseErrorCode;

public enum LoginErrorCode implements BaseErrorCode {

    BAD_REQUEST("EMAIL_LOGIN_01","잘못된 형식의 요청입니다."),
    FCM_TOKEN("EMAIL_LOGIN_02","FcmToken 이 누락되었습니다."),
    ALREADY_LOGIN("EMAIL_LOGIN_03","이미 로그인한 유저입니다."),
    INVALID("EMAIL_LOGIN_04","이메일 혹은 비밀번호가 잘못되었습니다.");

    private String code;
    private String message;

    LoginErrorCode(String code, String message) {
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
