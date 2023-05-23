package com.project.runningcrew.exception.notFound;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super(ResourceNotFoundErrorCode.USER_NOT_FOUND);
    }
}
