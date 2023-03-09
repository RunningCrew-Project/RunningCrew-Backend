package com.project.runningcrew.exception.notFound;

public class RefreshTokenNotFoundException extends ResourceNotFoundException {
    public RefreshTokenNotFoundException() {
        super("존재하지 않는 리프레시 토큰입니다.");
    }
}
