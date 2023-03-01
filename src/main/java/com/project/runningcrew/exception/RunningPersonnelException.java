package com.project.runningcrew.exception;

public class RunningPersonnelException extends RuntimeException {
    public RunningPersonnelException() {
        super("런닝 인원이 가득 찼습니다.");
    }
}
