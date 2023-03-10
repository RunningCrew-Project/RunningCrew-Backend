package com.project.runningcrew.exception.jwt;

public class JwtExpiredException extends RuntimeException{
    public JwtExpiredException() {
        super("토큰 값이 만료되었습니다.");
    }
}
