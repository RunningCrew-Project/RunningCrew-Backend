package com.project.runningcrew.exception;

public class S3UploadException extends RuntimeException {
    public S3UploadException() {
        super("S3 버킷에 이미지 업로드 중 문제가 발생했습니다.");
    }
}
