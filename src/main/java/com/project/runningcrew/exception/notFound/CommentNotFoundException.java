package com.project.runningcrew.exception.notFound;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException() {
        super(ResourceNotFoundErrorCode.COMMENT_NOT_FOUND);
    }
}
