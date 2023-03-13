package com.project.runningcrew.exception.duplicate;

public class UserEmailDuplicateException extends DuplicateException{

    public UserEmailDuplicateException() {
        super("이미 가입된 이메일 주소입니다.");
    }

    public UserEmailDuplicateException(String email) {
        super("이미 가입된 이메일 주소입니다.", "email", email);
    }

}
