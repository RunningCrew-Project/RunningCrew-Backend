package com.project.runningcrew.exception.badinput;

public class CrewJoinQuestionException extends BadInputException {

    public CrewJoinQuestionException() {
        super("가입 질문이 설정되어 있지 않은 크루입니다.");
    }

}
