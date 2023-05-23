package com.project.runningcrew.exception.image.s3;

import com.project.runningcrew.exception.image.ImageErrorCode;

public class S3UploadException extends S3ImageException {

    public S3UploadException(String bucketName) {
        super(ImageErrorCode.S3_IMAGE_UPLOAD, bucketName);
    }

}
