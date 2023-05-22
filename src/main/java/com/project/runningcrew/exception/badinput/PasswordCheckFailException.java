package com.project.runningcrew.exception.badinput;


public class PasswordCheckFailException extends BadInputException {
    public PasswordCheckFailException() {
        super(BadInputErrorCode.PASSWORD_CHECK_FAIL);
    }
}
