package com.project.runningcrew.exception.duplicate;


public class RunningMemberDuplicateException extends DuplicateException {
    public RunningMemberDuplicateException(Long runningNoticeId) {
        super(DuplicateErrorCode.RUNNING_MEMBER_DUPLICATE, "runningNoticeId", runningNoticeId);
    }
}
