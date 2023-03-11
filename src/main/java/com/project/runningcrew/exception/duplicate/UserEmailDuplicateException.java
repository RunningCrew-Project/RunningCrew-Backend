package com.project.runningcrew.exception.duplicate;

public class UserEmailDuplicateException extends DuplicateException{

    private String email;

    public UserEmailDuplicateException() {
        super("이미 가입된 이메일 주소입니다.");
    }

    public UserEmailDuplicateException(String email) {
        super("이미 가입된 이메일 주소입니다.");
        this.email = email;
    }

}
