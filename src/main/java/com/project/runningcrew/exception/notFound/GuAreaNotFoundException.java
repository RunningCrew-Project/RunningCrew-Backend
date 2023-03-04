package com.project.runningcrew.exception.notFound;

public class GuAreaNotFoundException extends ResourceNotFoundException{
    public GuAreaNotFoundException() {
        super("존재하지 않는 구입니다.");
    }
}
