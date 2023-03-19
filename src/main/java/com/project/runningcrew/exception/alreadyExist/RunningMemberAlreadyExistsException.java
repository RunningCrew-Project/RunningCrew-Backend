package com.project.runningcrew.exception.alreadyExist;

public class RunningMemberAlreadyExistsException extends AlreadyExistsException {
    public RunningMemberAlreadyExistsException(Long runningNoticeId) {
        super("멤버가 이미 신청한 런닝공지입니다.","runningNoticeId", runningNoticeId);
    }
}
