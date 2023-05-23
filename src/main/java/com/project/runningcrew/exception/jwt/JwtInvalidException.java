package com.project.runningcrew.exception.jwt;

import com.project.runningcrew.exception.common.BaseException;

public class JwtInvalidException extends BaseException {
    public JwtInvalidException() {
        super(JwtErrorCode.JWT_INVALID);
    }
}
