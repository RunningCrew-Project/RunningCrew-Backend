package com.project.runningcrew.exception.image;

import lombok.Getter;

import java.util.Map;

@Getter
public class ImageException extends RuntimeException {

    protected Map<String, String> errors = Map.of();

    public ImageException(String message) {
        super(message);
    }

}
