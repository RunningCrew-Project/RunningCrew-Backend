package com.project.runningcrew.exception.notFound;

public class FcmTokenNotFoundException extends ResourceNotFoundException {
    public FcmTokenNotFoundException() {
        super("존재하지 않는 FCM 토큰입니다.");
    }
}
