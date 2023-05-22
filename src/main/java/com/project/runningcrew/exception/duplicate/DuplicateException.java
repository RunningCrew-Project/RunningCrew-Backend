package com.project.runningcrew.exception.duplicate;

import com.project.runningcrew.exception.common.BaseException;
import com.project.runningcrew.exception.common.BaseErrorCode;
import lombok.Getter;

@Getter
public class DuplicateException extends BaseException {

    private String type;
    private String value;

    public DuplicateException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateException(BaseErrorCode errorCode, String type, Long value) {
        super(errorCode);
        this.type = type;
        this.value = value.toString();
    }

    public DuplicateException(BaseErrorCode errorCode, String type, String value) {
        super(errorCode);
        this.type = type;
        this.value = value;
    }

}
