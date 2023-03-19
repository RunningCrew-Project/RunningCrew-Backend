package com.project.runningcrew.recruitanswer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateRecruitAnswerList<T> {

    @Schema(description = "가입 답변 정보")
    private List<T> answers = new ArrayList<>();

}
