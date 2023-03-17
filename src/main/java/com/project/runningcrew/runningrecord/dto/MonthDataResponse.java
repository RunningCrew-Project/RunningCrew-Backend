package com.project.runningcrew.runningrecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MonthDataResponse {

    @Schema(description = "런닝 누적 거리", example = "22.12")
    private double totalRunningDistance;

    @Schema(description = "런닝 누적 시간", example = "22.12")
    private int totalRunningTime;

}
