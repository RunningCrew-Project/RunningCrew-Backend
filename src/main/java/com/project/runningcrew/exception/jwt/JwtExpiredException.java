package com.project.runningcrew.exception.jwt;

import com.project.runningcrew.exception.common.BaseException;

public class JwtExpiredException extends BaseException {
    public JwtExpiredException() {
        super(JwtErrorCode.JWT_EXPIRED);
    }
}
