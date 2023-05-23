package com.project.runningcrew.exception.notFound;

public class MemberNotFoundException extends ResourceNotFoundException{
    public MemberNotFoundException() {
        super(ResourceNotFoundErrorCode.MEMBER_NOT_FOUND);
    }
}
