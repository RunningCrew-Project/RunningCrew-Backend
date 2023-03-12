package com.project.runningcrew.exception.badinput;

import java.util.Map;

public class GuFullNameException extends BadInputException {

    public GuFullNameException(String fullName) {
        super("올바르지 않은 형식의 fullName 입니다.", Map.of("fullName", fullName));
    }

}
