package com.project.runningcrew.exception.image.local;

import com.project.runningcrew.exception.image.ImageErrorCode;
import com.project.runningcrew.exception.image.ImageException;

public class LocalImageDeleteException extends ImageException {

    public LocalImageDeleteException(String filePath) {
        super(ImageErrorCode.LOCAL_IMAGE_DELETE);
        super.getErrors().put("filePath", filePath);
    }

}
