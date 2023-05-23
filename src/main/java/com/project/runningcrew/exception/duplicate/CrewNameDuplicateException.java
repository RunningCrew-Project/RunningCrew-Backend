package com.project.runningcrew.exception.duplicate;

public class CrewNameDuplicateException extends DuplicateException {

    public CrewNameDuplicateException() {
        super(DuplicateErrorCode.CREW_NAME_DUPLICATE);
    }

    public CrewNameDuplicateException(String crewName) {
        super(DuplicateErrorCode.CREW_NAME_DUPLICATE, "crew", crewName);
    }

}
