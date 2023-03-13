package com.project.runningcrew.exception.notFound;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException() {
        super("존재하지 않는 댓글입니다.");
    }
}
