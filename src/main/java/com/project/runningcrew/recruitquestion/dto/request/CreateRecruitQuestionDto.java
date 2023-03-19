package com.project.runningcrew.recruitquestion.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateRecruitQuestionDto {

    @Schema(description = "가입 질문 내용", example = "question")
    @NotBlank(message = "가입 질문 내용은 필수 값입니다.")
    @Size(min = 1, max = 200, message = "가입 질문은 1자 이상 200자 이하입니다.")
    private String question;

    @Schema(description = "가입 질문 순서", example = "1")
    @Positive(message = "가입 질문 순서는 1 이상의 수입니다.")
    private int questionOffset;

}
