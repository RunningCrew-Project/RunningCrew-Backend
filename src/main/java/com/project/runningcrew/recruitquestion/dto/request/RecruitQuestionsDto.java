package com.project.runningcrew.recruitquestion.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecruitQuestionsDto {

    @Valid
    @Size(max = 3, message = "가입 질문들의 개수는 3 이하입니다.")
    @Schema(description = "설정할 가입 질문들")
    List<CreateRecruitQuestionDto> questions;

}
