package com.project.runningcrew.exception.image.s3;

import com.project.runningcrew.exception.image.ImageErrorCode;

import java.util.Map;

public class S3DeleteException extends S3ImageException {

    public S3DeleteException(String bucketName, String fileName) {
        super(ImageErrorCode.S3_IMAGE_DELETE, bucketName);
        this.getErrors().put("fileName", fileName);
    }

}
