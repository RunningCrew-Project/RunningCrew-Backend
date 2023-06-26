package com.project.runningcrew.exception.badinput;


public class FcmTokenEmptyException extends BadInputException {

    public FcmTokenEmptyException() {
        super(BadInputErrorCode.FCM_EMPTY);
    }

}
