package com.project.runningcrew.exception.duplicate;

import com.project.runningcrew.exception.common.BaseErrorCode;

public enum DuplicateErrorCode implements BaseErrorCode {

    MEMBER_DUPLICATE("MEM_01", "이미 존재하는 멤버입니다."),
    RUNNING_MEMBER_DUPLICATE("RUNMEM_01", "멤버가 이미 신청한 런닝공지입니다."),
    CREW_NAME_DUPLICATE("CREW_01","이미 존재하는 크루 이름입니다."),
    USER_EMAIL_DUPLICATE("USER_01","이미 가입된 이메일 주소입니다."),
    USER_NICKNAME_DUPLICATE("USER_02","이미 사용중인 닉네임입니다.");

    private String code;
    private String message;

    DuplicateErrorCode(String code, String message) {
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
