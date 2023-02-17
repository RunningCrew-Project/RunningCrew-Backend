package com.project.runningcrew.exception;

public class CommentNotChangeException extends NotChangeException{
    public CommentNotChangeException() {
        super("내용이 변경되지 않았습니다.");
    }
}
