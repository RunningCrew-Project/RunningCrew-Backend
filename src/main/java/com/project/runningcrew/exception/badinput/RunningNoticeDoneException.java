package com.project.runningcrew.exception.badinput;

import com.project.runningcrew.runningnotice.entity.RunningStatus;

import java.util.Map;

public class RunningNoticeDoneException extends BadInputException {

    public RunningNoticeDoneException() {
        super(BadInputErrorCode.RUNNING_NOTICE_DONE,
                Map.of("runningStatus", RunningStatus.DONE.toString()));
    }

}
