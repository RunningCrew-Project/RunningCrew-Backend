package com.project.runningcrew.exception.notFound;

public class RunningNoticeNotFoundException extends ResourceNotFoundException {
    public RunningNoticeNotFoundException() {
        super(ResourceNotFoundErrorCode.RUNNING_NOTICE_NOT_FOUND);
    }
}
