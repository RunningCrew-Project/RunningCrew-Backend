package com.project.runningcrew.exception.image.local;

import com.project.runningcrew.exception.image.ImageErrorCode;
import com.project.runningcrew.exception.image.ImageException;

public class LocalImageUploadException extends ImageException {

    public LocalImageUploadException(String filePath) {
        super(ImageErrorCode.LOCAL_IMAGE_UPLOAD);
        this.getErrors().put("filePath", filePath);
    }

}
