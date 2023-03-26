package com.project.runningcrew.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class YearMonthDto {

    @Schema(description = "년도", example = "2023")
    @Positive(message = "년도는 1 이상의 정수입니다.")
    private int year;

    @Schema(description = "월", example = "3")
    @Min(value = 1, message = "월은 1 이상의 정수입니다.")
    @Max(value = 12, message = "월은 12 이하의 정수입니다.")
    @Positive(message = "월은 1 이상의 정수입니다.")
    private int month;

}
