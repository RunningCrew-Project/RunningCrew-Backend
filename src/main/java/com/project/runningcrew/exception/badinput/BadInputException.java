package com.project.runningcrew.exception.badinput;

import java.util.Map;

public class BadInputException extends RuntimeException{
    private Map<String, String> badInputMaps;

    public BadInputException(String message) {
        super(message);
    }

    public BadInputException(String message, Map<String, String> badInputMaps) {
        super(message);
        this.badInputMaps = badInputMaps;
    }

}
