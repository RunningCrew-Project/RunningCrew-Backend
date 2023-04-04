package com.project.runningcrew.exception.image.s3;

import java.util.Map;

public class S3DeleteException extends S3ImageException {

    public S3DeleteException(String bucketName, String fileName) {
        super("S3 버킷에 존재하지 않는 이미지입니다.", bucketName);
        this.getErrors().put("fileName", fileName);
    }

}
