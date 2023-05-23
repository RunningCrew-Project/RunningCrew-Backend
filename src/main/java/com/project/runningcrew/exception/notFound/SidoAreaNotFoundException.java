package com.project.runningcrew.exception.notFound;

public class SidoAreaNotFoundException extends ResourceNotFoundException{
    public SidoAreaNotFoundException() {
        super(ResourceNotFoundErrorCode.SIDO_AREA_NOT_FOUND);
    }
}
