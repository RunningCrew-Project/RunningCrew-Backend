package com.project.runningcrew.exception.notFound;

public class RecruitQuestionNotFoundException extends ResourceNotFoundException{
    public RecruitQuestionNotFoundException() {
        super("존재하지 않는 가입 질문입니다.");
    }
}
