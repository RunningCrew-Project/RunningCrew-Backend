package com.project.runningcrew.crew.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCrewResponse {

    @Schema(description = "크루 아이디", example = "1")
    private Long id;

    @Schema(description = "크루 생성 일자", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "크루 이름", example = "crew")
    private String name;

    @Schema(description = "크루 소개글", example = "introduction")
    private String introduction;

    @Schema(description = "크루 이미지", example = "crewImgUrl")
    private String crewImgUrl;

    @Schema(description = "크루 활동 지역", example = "서울시 동대문구 전농동")
    private String dong;

    @Schema(description = "크루 멤버수", example = "33")
    private Long memberCount;

    @Schema(description = "가입 신청 받기 여부", example = "true")
    private boolean joinApply;

    @Schema(description = "가입 질문 여부", example = "false")
    private boolean joinQuestion;

    public GetCrewResponse(Crew crew, Long memberCount, CrewCondition crewCondition) {
        this.id = crew.getId();
        this.createdDate = crew.getCreatedDate();
        this.name = crew.getName();
        this.introduction = crew.getIntroduction();
        this.crewImgUrl = crew.getCrewImgUrl();
        this.dong = crew.getDongArea().getFullName();
        this.memberCount = memberCount;
        this.joinApply = crewCondition.isJoinApply();
        this.joinQuestion = crewCondition.isJoinQuestion();
    }

}

