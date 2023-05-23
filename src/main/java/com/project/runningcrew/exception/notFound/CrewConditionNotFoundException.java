package com.project.runningcrew.exception.notFound;

public class CrewConditionNotFoundException extends ResourceNotFoundException {
    public CrewConditionNotFoundException() {
        super(ResourceNotFoundErrorCode.CREW_CONDITION_NOT_FOUND);
    }
}
