package com.project.runningcrew.exception.image.local;

import com.project.runningcrew.exception.image.ImageException;

public class LocalImageUploadException extends ImageException {

    public LocalImageUploadException(String filePath) {
        super("이미지 파일 업로드 중 문제가 발생했습니다.");
        this.getErrors().put("filePath", filePath);
    }

}
