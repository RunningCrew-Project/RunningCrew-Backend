package com.project.runningcrew.exception.notFound;

public class CrewConditionNotFoundException extends ResourceNotFoundException {
    public CrewConditionNotFoundException() {
        super("존재하지 않는 크루 조건입니다.");
    }
}
