package com.project.runningcrew.exception.notFound;

public class UserRoleNotFoundException extends ResourceNotFoundException{
    public UserRoleNotFoundException() {
        super(ResourceNotFoundErrorCode.USER_ROLE_NOT_FOUND);
    }
}
