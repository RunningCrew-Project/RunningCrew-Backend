package com.project.runningcrew.exception.badinput;

import java.time.LocalDateTime;
import java.util.Map;

public class RunningDateTimeBeforeException extends BadInputException {

    public RunningDateTimeBeforeException(LocalDateTime now, LocalDateTime runningDateTime) {
        super(BadInputErrorCode.RUNNING_DATE_TIME_BEFORE,
                Map.of("now", now.toString(),
                        "runningDateTime", runningDateTime.toString()));
    }

}
