package com.project.runningcrew.crew.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CrewListResponse<T> {

    @Schema(description = "크루 리스트")
    private List<T> crews;

}
