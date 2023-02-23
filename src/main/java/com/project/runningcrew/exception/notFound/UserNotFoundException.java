package com.project.runningcrew.exception.notFound;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super("존재하지 않는 유저입니다.");
    }
}
