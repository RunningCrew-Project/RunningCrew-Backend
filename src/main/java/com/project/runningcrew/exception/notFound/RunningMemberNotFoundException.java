package com.project.runningcrew.exception.notFound;

public class RunningMemberNotFoundException extends ResourceNotFoundException {
    public RunningMemberNotFoundException() {
        super(ResourceNotFoundErrorCode.RUNNING_MEMBER_NOT_FOUND);
    }
}
