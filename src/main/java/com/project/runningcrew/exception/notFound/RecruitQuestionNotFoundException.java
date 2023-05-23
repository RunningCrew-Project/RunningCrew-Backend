package com.project.runningcrew.exception.notFound;

public class RecruitQuestionNotFoundException extends ResourceNotFoundException{
    public RecruitQuestionNotFoundException() {
        super(ResourceNotFoundErrorCode.RECRUIT_QUESTION_NOT_FOUND);
    }
}
