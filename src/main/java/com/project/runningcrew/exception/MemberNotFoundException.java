package com.project.runningcrew.exception;

public class MemberNotFoundException extends ResourceNotFoundException{
    public MemberNotFoundException() {
        super("존재하지 않는 멤버입니다.");
    }
}
