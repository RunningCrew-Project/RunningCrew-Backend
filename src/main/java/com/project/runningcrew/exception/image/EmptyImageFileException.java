package com.project.runningcrew.exception.image;

public class EmptyImageFileException extends ImageException {

    public EmptyImageFileException() {
        super(ImageErrorCode.EMPTY_IMAGE_FILE);
    }

}
