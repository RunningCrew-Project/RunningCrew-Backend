package com.project.runningcrew.exception.image;

public class EmptyImageFileException extends ImageException {

    public EmptyImageFileException() {
        super("빈 이미지 파일입니다.");
    }

}
