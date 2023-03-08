package com.project.runningcrew.crew.dto;

import com.project.runningcrew.crew.entity.Crew;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleCrewDto {

    @Schema(description = "크루 id", example = "1")
    private Long id;

    @Schema(description = "크루 이름", example = "crew")
    private String name;

    @Schema(description = "크루 소개", example = "introduction")
    private String introduction;

    @Schema(description = "크루 이미지", example = "imgUrl")
    private String crewImgUrl;

    @Schema(description = "크루 멤버수", example = "33")
    private Long memberCount;

    public SimpleCrewDto(Crew crew, Long memberCount) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.introduction = crew.getIntroduction();
        this.crewImgUrl = crew.getCrewImgUrl();
        this.memberCount = memberCount;
    }

}
