package com.project.runningcrew.exception.notFound;

public class GuAreaNotFoundException extends ResourceNotFoundException{
    public GuAreaNotFoundException() {
        super(ResourceNotFoundErrorCode.GU_AREA_NOT_FOUND);
    }
}
