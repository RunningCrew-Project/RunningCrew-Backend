package com.project.runningcrew.exception.image;

import com.project.runningcrew.exception.common.BaseErrorCode;
import com.project.runningcrew.exception.common.BaseException;
import lombok.Getter;

import java.util.Map;

@Getter
public class ImageException extends BaseException {

    protected Map<String, String> errors = Map.of();

    public ImageException(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
