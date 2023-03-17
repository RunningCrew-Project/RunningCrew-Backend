package com.project.runningcrew.recruitquestion.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetRecruitQuestionList<T> {

    @Schema(description = "조회 가입 질문 정보")
    List<T> questions;

}
