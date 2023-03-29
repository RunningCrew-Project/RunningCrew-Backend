package com.project.runningcrew.recruitanswer.dto.response;

import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetRecruitAnswerDto {

    @Schema(description = "가입 답변 아이디", example = "1")
    private Long id;

    @Schema(description = "가입 답변 내용", example = "answer")
    private String answer;

    @Schema(description = "가입 답변 순서", example = "1")
    private int answerOffset;

    public GetRecruitAnswerDto(RecruitAnswer answer) {
        this.id = answer.getId();
        this.answer = answer.getAnswer();
        this.answerOffset = answer.getAnswerOffset();
    }

}
