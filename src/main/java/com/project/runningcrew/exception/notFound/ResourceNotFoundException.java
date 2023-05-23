package com.project.runningcrew.exception.notFound;

import com.project.runningcrew.exception.common.BaseErrorCode;
import com.project.runningcrew.exception.common.BaseException;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
