package com.project.runningcrew.recruitquestion.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetExistOfRecruitQuestion {

    @Schema(description = "크루의 가입 질문 설정 여부", example = "false")
    private boolean setQuestion;

    public GetExistOfRecruitQuestion(Boolean isSet) {
        this.setQuestion = isSet;
    }

}
