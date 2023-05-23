package com.project.runningcrew.exception.notFound;

public class RefreshTokenNotFoundException extends ResourceNotFoundException {
    public RefreshTokenNotFoundException() {
        super(ResourceNotFoundErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
