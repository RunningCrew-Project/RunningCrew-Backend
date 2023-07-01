package com.project.runningcrew.exception.badinput;

import com.project.runningcrew.exception.common.BaseErrorCode;

public enum BadInputErrorCode implements BaseErrorCode {

    CREW_JOIN_APPLY("CREW_02", "신규멤버를 모집하지 않는 크루입니다."),
    CREW_JOIN_QUESTION("CREW_03", "가입 질문이 설정되어 있지 않은 크루입니다."),
    GU_FULL_NAME("GU_01", "올바르지 않은 형식의 fullName 입니다."),
    RUNNING_DATE_TIME_AFTER("RUNNOT_01", "런닝시간이 지났습니다."),
    RUNNING_DATE_TIME_BEFORE("RUNNOT_02", "런닝시간 이전입니다."),
    RUNNING_NOTICE_DONE("RUNNOT_03", "이미 종료된 런닝입니다."),
    RUNNING_PERSONNEL("RUNNOT_04", "런닝 인원이 가득 찼습니다."),
    UPDATE_MEMBER_ROLE("MEM_02", "수정할 수 없는 멤버의 권한을 가지고 있습니다."),
    YEAR_MONTH_FORMAT("COM_02", "잘못된 형식의 년, 월 입니다."),
    PASSWORD_CHECK_FAIL("USER_03", "비밀번호가 이전 값과 일치하지 않습니다."),
    FCM_EMPTY("FCM_01", "FCM 토큰값이 비어있습니다.");

    private String code;
    private String message;

    BadInputErrorCode(String code, String message) {
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
