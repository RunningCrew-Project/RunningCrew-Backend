package com.project.runningcrew.exception;

public class CrewNotFoundException extends ResourceNotFoundException {
    public CrewNotFoundException() {
        super("존재하지 않는 크루입니다.");
    }
}
