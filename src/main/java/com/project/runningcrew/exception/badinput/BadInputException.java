package com.project.runningcrew.exception.badinput;

import lombok.Getter;

import java.util.Map;

@Getter
public class BadInputException extends RuntimeException{
    private Map<String, String> badInputMaps = Map.of();

    public BadInputException(String message) {
        super(message);
    }

    public BadInputException(String message, Map<String, String> badInputMaps) {
        super(message);
        this.badInputMaps = badInputMaps;
    }

}
