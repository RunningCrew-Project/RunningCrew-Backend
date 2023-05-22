package com.project.runningcrew.exception.duplicate;

public class UserNickNameDuplicateException extends DuplicateException {

    public UserNickNameDuplicateException() {
        super(DuplicateErrorCode.USER_NICKNAME_DUPLICATE);
    }

    public UserNickNameDuplicateException(String nickname) {
        super(DuplicateErrorCode.USER_NICKNAME_DUPLICATE, "nickname", nickname);
    }

}
