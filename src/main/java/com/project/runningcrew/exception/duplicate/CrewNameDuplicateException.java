package com.project.runningcrew.exception.duplicate;

public class CrewNameDuplicateException extends DuplicateException {

    public CrewNameDuplicateException() {
        super("이미 존재하는 크루 이름입니다.");
    }

    public CrewNameDuplicateException(String crewName) {
        super("이미 존재하는 크루 이름입니다.", "crew", crewName);
    }

}
