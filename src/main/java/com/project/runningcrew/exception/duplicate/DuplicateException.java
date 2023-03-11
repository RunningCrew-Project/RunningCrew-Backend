package com.project.runningcrew.exception.duplicate;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException{

    private String type;
    private String value;

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, String type, String value) {
        super(message);
        this.type = type;
        this.value = value;
    }

}
