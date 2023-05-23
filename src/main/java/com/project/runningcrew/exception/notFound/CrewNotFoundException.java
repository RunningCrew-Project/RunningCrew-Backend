package com.project.runningcrew.exception.notFound;

public class CrewNotFoundException extends ResourceNotFoundException {
    public CrewNotFoundException() {
        super(ResourceNotFoundErrorCode.CREW_NOT_FOUND);
    }
}
