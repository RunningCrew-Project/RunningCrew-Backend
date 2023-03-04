package com.project.runningcrew.exception.notFound;

public class DongAreaNotFoundException extends ResourceNotFoundException {
    public DongAreaNotFoundException() {
        super("존재하지 않는 동입니다.");
    }
}
