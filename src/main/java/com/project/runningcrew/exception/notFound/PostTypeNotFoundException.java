package com.project.runningcrew.exception.notFound;

public class PostTypeNotFoundException extends ResourceNotFoundException {

    public PostTypeNotFoundException() {
        super("존재하지 않는 글 종류입니다.");
    }

}
