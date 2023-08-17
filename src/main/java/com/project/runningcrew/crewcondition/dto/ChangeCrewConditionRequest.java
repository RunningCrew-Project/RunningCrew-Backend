package com.project.runningcrew.crewcondition.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class ChangeCrewConditionRequest {

    @NotNull
    @Schema(description = "크루 가입 가능 여부", example = "true")
    private boolean joinApply;

    @NotNull
    @Schema(description = "크루 가입 질문 여부", example = "false")
    private boolean joinQuestion;

}
