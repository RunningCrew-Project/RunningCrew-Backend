package com.project.runningcrew.recruitanswer.dto.request;

import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class CreateRecruitAnswerDto {

    @Schema(description = "가입 답변 내용", example = "answer")
    @NotBlank(message = "가입 답변 내용은 필수 값입니다.")
    @Size(min = 1, max = 200, message = "가입 답변은 1자 이상 200자 이하입니다.")
    private String answer;

    @Schema(description = "가입 답변 순서", example = "1")
    @Positive(message = "답변 순서는 1 이상의 수입니다.")
    private int answerOffset;

}
