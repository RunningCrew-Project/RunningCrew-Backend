package com.project.runningcrew.exception.s3;

public class S3DeleteException extends RuntimeException {
    public S3DeleteException() {
        super("S3 버킷에 존재하지 않는 이미지입니다.");
    }
}
