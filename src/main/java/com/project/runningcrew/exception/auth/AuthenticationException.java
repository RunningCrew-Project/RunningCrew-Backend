package com.project.runningcrew.exception.auth;

import com.project.runningcrew.exception.common.BaseException;

public class AuthenticationException extends BaseException {
    public AuthenticationException() {
        super(AuthErrorCode.AUTHENTICATION);
    }
}
