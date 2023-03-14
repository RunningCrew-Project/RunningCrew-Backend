package com.project.runningcrew.exception.alreadyExist;

public class MemberAlreadyExistsException extends AlreadyExistsException {

    public MemberAlreadyExistsException() {
        super("이미 존재하는 멤버입니다.");
    }

    public MemberAlreadyExistsException(Long memberId) {
        super("이미 존재하는 멤버입니다.", "memberId", memberId);
    }

}
