package com.project.runningcrew.exception.image;

import com.project.runningcrew.exception.common.BaseErrorCode;

public enum ImageErrorCode implements BaseErrorCode {

    EMPTY_IMAGE_FILE("IMG_01", "빈 이미지 파일입니다."),
    LOCAL_IMAGE_DELETE("IMG_02", "존재하지 않는 이미지 파일입니다."),
    LOCAL_IMAGE_UPLOAD("IMG_03", "이미지 파일 업로드 중 문제가 발생했습니다."),
    S3_IMAGE_DELETE("IMG_04", "S3 버킷에 존재하지 않는 이미지입니다."),
    S3_IMAGE_UPLOAD("IMG_05", "S3 버킷에 이미지 업로드 중 문제가 발생했습니다.");

    private String code;
    private String message;

    ImageErrorCode(String code, String message) {
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
