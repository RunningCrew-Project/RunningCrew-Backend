package com.project.runningcrew.exception.badinput;

import com.project.runningcrew.runningnotice.entity.RunningStatus;

import java.util.Map;

public class RunningNoticeDoneException extends BadInputException {

    public RunningNoticeDoneException() {
        super("이미 종료된 런닝입니다.", Map.of("runningStatus", RunningStatus.DONE.toString()));
    }

}
