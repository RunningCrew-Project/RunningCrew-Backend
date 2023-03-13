package com.project.runningcrew.exception.duplicate;

public class UserNickNameDuplicateException extends DuplicateException{

    public UserNickNameDuplicateException() {
        super("이미 사용중인 닉네임입니다.");
    }

    public UserNickNameDuplicateException(String nickname) {
        super("이미 사용중인 닉네임입니다.", "nickname", nickname);
    }

}
