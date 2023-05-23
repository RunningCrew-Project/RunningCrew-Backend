package com.project.runningcrew.exception.duplicate;

public class UserEmailDuplicateException extends DuplicateException {

    public UserEmailDuplicateException() {
        super(DuplicateErrorCode.USER_EMAIL_DUPLICATE);
    }

    public UserEmailDuplicateException(String email) {
        super(DuplicateErrorCode.USER_EMAIL_DUPLICATE, "email", email);
    }

}
