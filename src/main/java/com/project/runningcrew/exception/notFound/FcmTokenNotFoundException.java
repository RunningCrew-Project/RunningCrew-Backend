package com.project.runningcrew.exception.notFound;

public class FcmTokenNotFoundException extends ResourceNotFoundException {
    public FcmTokenNotFoundException() {
        super(ResourceNotFoundErrorCode.FCM_TOKEN_NOT_FOUND);
    }
}
