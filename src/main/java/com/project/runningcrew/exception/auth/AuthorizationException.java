package com.project.runningcrew.exception.auth;

import com.project.runningcrew.exception.common.BaseException;

public class AuthorizationException extends BaseException {
    public AuthorizationException() {
        super(AuthErrorCode.AUTHORIZATION);
    }
}
