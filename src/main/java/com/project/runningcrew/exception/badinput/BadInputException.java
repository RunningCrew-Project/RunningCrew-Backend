package com.project.runningcrew.exception.badinput;

import com.project.runningcrew.exception.common.BaseException;
import com.project.runningcrew.exception.common.BaseErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class BadInputException extends BaseException {
    private Map<String, String> badInputMaps = Map.of();

    public BadInputException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public BadInputException(BaseErrorCode errorCode, Map<String, String> badInputMaps) {
        super(errorCode);
        this.badInputMaps = badInputMaps;
    }

}
