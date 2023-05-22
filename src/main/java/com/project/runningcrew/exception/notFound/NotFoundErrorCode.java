package com.project.runningcrew.exception.notFound;

import com.project.runningcrew.exception.common.BaseErrorCode;

public enum NotFoundErrorCode implements BaseErrorCode {

    BLOCKED_INFO_NOT_FOUND("", ""),
    BOARD_NOT_FOUND("", ""),
    COMMENT_NOT_FOUND("", ""),
    CREW_CONDITION_NOT_FOUND("", ""),
    CREW_NOT_FOUND("", ""),
    DONG_AREA_NOT_FOUND("", ""),
    FCM_TOKEN_NOT_FOUND("", ""),
    GU_AREA_NOT_FOUND("", ""),
    IMAGE_NOT_FOUND("", ""),
    MEMBER_NOT_FOUND("", ""),
    POST_TYPE_NOT_FOUND("", ""),
    RECRUIT_QUESTION_NOT_FOUND("", ""),
    REFRESH_TOKEN_NOT_FOUND("", ""),
    REPORT_TYPE_NOT_FOUND("", ""),
    RUNNING_MEMBER_NOT_FOUND("", ""),
    RUNNING_NOTICE_NOT_FOUND("", ""),
    RUNNING_RECORD_NOT_FOUND("", ""),
    SIDO_AREA_NOT_FOUND("", ""),
    USER_NOT_FOUND("", ""),
    USER_ROLE_NOT_FOUND("", "");

    private String code;
    private String message;

    NotFoundErrorCode(String code, String message) {
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
