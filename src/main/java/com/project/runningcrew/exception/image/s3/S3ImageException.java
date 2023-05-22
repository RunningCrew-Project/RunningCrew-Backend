package com.project.runningcrew.exception.image.s3;

import com.project.runningcrew.exception.common.BaseErrorCode;
import com.project.runningcrew.exception.image.ImageException;

public class S3ImageException extends ImageException {

    public S3ImageException(BaseErrorCode errorCode, String bucketName) {
        super(errorCode);
        super.getErrors().put("bucketName", bucketName);
    }

}
