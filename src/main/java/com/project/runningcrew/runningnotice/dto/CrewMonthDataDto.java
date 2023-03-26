package com.project.runningcrew.runningnotice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CrewMonthDataDto {

    @Schema(description = "누적 정기런닝 횟수", example = "10")
    private int totalRunningCount;

    @Schema(description = "누적 참여인원", example = "55")
    private long totalRunningMember;

    @Schema(description = "런닝 일자", example = "[1, 3, 10, 11, 25]")
    private List<Integer> runningDates;

}
