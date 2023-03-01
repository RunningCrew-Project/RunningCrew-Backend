package com.project.runningcrew.exception.notFound;

public class BoardNotFoundException extends ResourceNotFoundException{
    public BoardNotFoundException() {
        super("존재하지 않는 게시글입니다.");
    }
}
