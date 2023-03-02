package com.project.runningcrew.exception;

public class RunningDateTimeException extends RuntimeException {
    public RunningDateTimeException() {
        super("런닝시간이 지났습니다.");
    }
}
