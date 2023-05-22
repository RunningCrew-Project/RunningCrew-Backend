package com.project.runningcrew.exception.jwt;

import com.project.runningcrew.exception.common.BaseException;

public class JwtVerificationException extends BaseException {
    public JwtVerificationException() {
        super(JwtErrorCode.JWT_VERIFICATION);
    }
}
