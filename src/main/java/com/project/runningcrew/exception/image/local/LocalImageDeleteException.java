package com.project.runningcrew.exception.image.local;

import com.project.runningcrew.exception.image.ImageException;

public class LocalImageDeleteException extends ImageException {

    public LocalImageDeleteException(String filePath) {
        super("존재하지 않는 이미지 파일입니다.");
        super.getErrors().put("filePath", filePath);
    }

}
