package com.project.runningcrew.exception.notFound;

public class UserRoleNotFoundException extends ResourceNotFoundException{
    public UserRoleNotFoundException() {
        super("존재하지 않는 유저 권한입니다.");
    }
}
