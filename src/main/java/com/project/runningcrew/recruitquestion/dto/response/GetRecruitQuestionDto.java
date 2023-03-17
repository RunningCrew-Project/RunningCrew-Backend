package com.project.runningcrew.recruitquestion.dto.response;

import com.project.runningcrew.recruitquestion.entity.RecruitQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetRecruitQuestionDto {

    @Schema(description = "가입 질문 아이디", example = "1")
    private Long id;

    @Schema(description = "가입 질문 내용", example = "question")
    private String question;

    @Schema(description = "가입 질문 순서", example = "1")
    private int questionOffset;

    public GetRecruitQuestionDto(RecruitQuestion question) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.questionOffset = question.getQuestionOffset();
    }

}
