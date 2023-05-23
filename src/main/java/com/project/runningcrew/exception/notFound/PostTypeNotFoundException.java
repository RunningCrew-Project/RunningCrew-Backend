package com.project.runningcrew.exception.notFound;

public class PostTypeNotFoundException extends ResourceNotFoundException {

    public PostTypeNotFoundException() {
        super(ResourceNotFoundErrorCode.POST_TYPE_NOT_FOUND);
    }

}
