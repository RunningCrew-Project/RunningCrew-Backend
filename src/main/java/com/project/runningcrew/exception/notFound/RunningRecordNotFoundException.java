package com.project.runningcrew.exception.notFound;

public class RunningRecordNotFoundException extends ResourceNotFoundException{
    public RunningRecordNotFoundException() {
        super(ResourceNotFoundErrorCode.RUNNING_RECORD_NOT_FOUND);
    }
}
