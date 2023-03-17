package com.project.runningcrew.recruitanswer.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetRecruitAnswerList<T> {

    @Schema(description = "조회 가입 답변 목록")
    List<T> answers = new ArrayList<>();

}
