package com.project.runningcrew.exception.badinput;

import java.time.LocalDateTime;
import java.util.Map;

public class RunningDateTimeBeforeException extends BadInputException{

    public RunningDateTimeBeforeException(LocalDateTime now, LocalDateTime runningDateTime) {
        super("런닝시간 이전입니다.", Map.of(
                "now", now.toString(),
                "runningDateTime", runningDateTime.toString()));
    }

}
