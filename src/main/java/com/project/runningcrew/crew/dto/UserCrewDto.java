package com.project.runningcrew.crew.dto;

import com.project.runningcrew.crew.entity.Crew;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserCrewDto {

    @Schema(description = "크루 id", example = "1")
    private Long id;

    @Schema(description = "크루 이름", example = "crew")
    private String name;

    @Schema(description = "크루 이미지", example = "imgUrl")
    private String crewImgUrl;

    public UserCrewDto(Crew crew) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.crewImgUrl = crew.getCrewImgUrl();
    }

}
