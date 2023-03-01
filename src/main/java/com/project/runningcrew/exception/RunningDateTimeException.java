package com.project.runningcrew.exception;

public class RunningDateTimeException extends RuntimeException {
    public RunningDateTimeException() {
        super("신청 시간이 지났습니다.");
    }
}
