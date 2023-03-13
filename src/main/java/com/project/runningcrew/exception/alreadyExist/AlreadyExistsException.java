package com.project.runningcrew.exception.alreadyExist;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {

    private String type;
    private Long value;

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String message, String type, Long value) {
        super(message);
        this.type = type;
        this.value = value;
    }

}
