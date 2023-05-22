package com.project.runningcrew.exception.duplicate;


public class MemberDuplicateException extends DuplicateException {

    public MemberDuplicateException() {
        super(DuplicateErrorCode.MEMBER_DUPLICATE);
    }

    public MemberDuplicateException(Long memberId) {
        super(DuplicateErrorCode.MEMBER_DUPLICATE, "memberId", memberId);
    }

}
