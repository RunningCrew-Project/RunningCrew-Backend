package com.project.runningcrew.exception.notFound;

import com.project.runningcrew.exception.common.BaseErrorCode;

public enum ResourceNotFoundErrorCode implements BaseErrorCode {

    BLOCKED_INFO_NOT_FOUND("BLOCK_00", "존재하지 않는 차단 정보입니다."),
    BOARD_NOT_FOUND("BOARD_00", "존재하지 않는 게시글입니다."),
    COMMENT_NOT_FOUND("COMMENT_00", "존재하지 않는 댓글입니다."),
    CREW_CONDITION_NOT_FOUND("CREWRL_00", "존재하지 않는 크루 조건입니다."),
    CREW_NOT_FOUND("CREW_00", "존재하지 않는 크루입니다."),
    DONG_AREA_NOT_FOUND("DONG_00", "존재하지 않는 동입니다."),
    FCM_TOKEN_NOT_FOUND("FCM_00", "존재하지 않는 FCM 토큰입니다."),
    GU_AREA_NOT_FOUND("GU_00", "존재하지 않는 구입니다."),
    IMAGE_NOT_FOUND("IMG_00", "존재하지 않는 이미지입니다."),
    MEMBER_NOT_FOUND("MEM_00", "존재하지 않는 멤버입니다."),
    POST_TYPE_NOT_FOUND("POST_00", "존재하지 않는 글 종류입니다."),
    RECRUIT_QUESTION_NOT_FOUND("RECQST_00", "존재하지 않는 가입 질문입니다."),
    REFRESH_TOKEN_NOT_FOUND("REFTKN_00", "존재하지 않는 리프레시 토큰입니다."),
    RUNNING_MEMBER_NOT_FOUND("RUNMEM_00", "존재하지 않는 런닝 멤버입니다."),
    RUNNING_NOTICE_NOT_FOUND("RUNNOT_00", "존재하지 않는 런닝공지입니다."),
    RUNNING_RECORD_NOT_FOUND("RUNRCD_00", "존재하지 않는 런닝 기록입니다."),
    SIDO_AREA_NOT_FOUND("SIDO_00", "존재하지 않는 시/도입니다."),
    USER_NOT_FOUND("USER_00", "존재하지 않는 유저입니다."),
    USER_ROLE_NOT_FOUND("USERRL_00", "존재하지 않는 유저 권한입니다.");

    private String code;
    private String message;

    ResourceNotFoundErrorCode(String code, String message) {
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
