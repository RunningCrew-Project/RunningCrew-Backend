package com.project.runningcrew.exception.badinput;

import java.time.LocalDateTime;
import java.util.Map;

public class RunningDateTimeAfterException extends BadInputException {

    public RunningDateTimeAfterException(LocalDateTime now, LocalDateTime runningDateTime) {
        super("런닝시간이 지났습니다.", Map.of(
                "now", now.toString(),
                "runningDateTime", runningDateTime.toString()));
    }

}
