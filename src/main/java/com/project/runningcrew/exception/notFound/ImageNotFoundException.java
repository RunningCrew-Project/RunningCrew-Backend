package com.project.runningcrew.exception.notFound;

public class ImageNotFoundException extends ResourceNotFoundException{
    public ImageNotFoundException() {
        super(ResourceNotFoundErrorCode.IMAGE_NOT_FOUND);
    }
}
