package com.project.runningcrew.exception;

public class RunningRecordNotFoundException extends ResourceNotFoundException{
    public RunningRecordNotFoundException() {
        super("존재하지 않는 런닝 기록입니다.");
    }
}
