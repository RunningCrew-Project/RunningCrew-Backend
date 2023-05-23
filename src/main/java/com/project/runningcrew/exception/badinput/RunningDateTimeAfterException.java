package com.project.runningcrew.exception.badinput;

import java.time.LocalDateTime;
import java.util.Map;

public class RunningDateTimeAfterException extends BadInputException {

    public RunningDateTimeAfterException(LocalDateTime now, LocalDateTime runningDateTime) {
        super(BadInputErrorCode.RUNNING_DATE_TIME_AFTER,
                Map.of("now", now.toString(),
                        "runningDateTime", runningDateTime.toString()));
    }

}
