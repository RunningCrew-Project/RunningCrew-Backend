package com.project.runningcrew.crewcondition.dto;

import com.project.runningcrew.crewcondition.entity.CrewCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCrewConditionResponse {

    @Schema(description = "가입 신청 받기", example = "true")
    private boolean joinApply;

    @Schema(description = "가입 질문", example = "false")
    private boolean joinQuestion;

    public GetCrewConditionResponse(CrewCondition crewCondition) {
        this.joinApply = crewCondition.isJoinApply();
        this.joinQuestion = crewCondition.isJoinQuestion();
    }

}
