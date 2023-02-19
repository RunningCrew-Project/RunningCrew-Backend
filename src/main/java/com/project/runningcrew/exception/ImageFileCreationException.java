package com.project.runningcrew.exception;

public class ImageFileCreationException extends RuntimeException {
    public ImageFileCreationException() {
        super("이미지 파일 생성 중 문제가 발생했습니다.");
    }
}
