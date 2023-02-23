package com.project.runningcrew.exception.notFound;

public class RunningMemberNotFoundException extends ResourceNotFoundException {
    public RunningMemberNotFoundException() {
        super("존재하지 않는 런닝 멤버입니다.");
    }
}
