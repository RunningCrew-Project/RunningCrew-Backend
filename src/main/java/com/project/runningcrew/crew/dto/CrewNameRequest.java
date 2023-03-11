package com.project.runningcrew.crew.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CrewNameRequest {

    @Schema(description = "크루 이름", example = "crew")
    @NotBlank(message = "크루 이름은 필수값입니다.")
    @Size(min = 1, max = 100, message = "크루 이름은 1 자 이상 100 자 이하입니다.")
    private String name;

}
