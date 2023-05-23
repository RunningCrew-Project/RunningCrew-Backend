package com.project.runningcrew.exception.badinput;

import java.util.Map;

public class GuFullNameException extends BadInputException {

    public GuFullNameException(String fullName) {
        super(BadInputErrorCode.GU_FULL_NAME, Map.of("fullName", fullName));
    }

}
