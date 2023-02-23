package com.project.runningcrew.exception.alreadyExist;

public class RunningMemberAlreadyExistsException extends AlreadyExistsException {
    public RunningMemberAlreadyExistsException() {
        super("멤버가 이미 신청한 런닝공지입니다.");
    }
}
