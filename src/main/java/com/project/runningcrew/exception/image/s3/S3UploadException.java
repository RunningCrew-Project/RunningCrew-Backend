package com.project.runningcrew.exception.image.s3;

public class S3UploadException extends S3ImageException {

    public S3UploadException(String bucketName) {
        super("S3 버킷에 이미지 업로드 중 문제가 발생했습니다.", bucketName);
    }

}
