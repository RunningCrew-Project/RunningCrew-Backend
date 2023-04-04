package com.project.runningcrew.exception.image.s3;

import com.project.runningcrew.exception.image.ImageException;

public class S3ImageException extends ImageException {

    public S3ImageException(String message, String bucketName) {
        super(message);
        super.getErrors().put("bucketName", bucketName);
    }

}
