package com.project.runningcrew.exception.notFound;

public class DongAreaNotFoundException extends ResourceNotFoundException {
    public DongAreaNotFoundException() {
        super(ResourceNotFoundErrorCode.DONG_AREA_NOT_FOUND);
    }
}
