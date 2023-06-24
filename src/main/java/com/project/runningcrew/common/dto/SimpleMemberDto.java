package com.project.runningcrew.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.runningcrew.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleMemberDto {

    @Schema(description = "작성자 아이디", example = "1")
    private Long id;

    @Schema(description = "작성자 권한", example = "크루원")
    private String role;

    @Schema(description = "작성자 계정 정보")
    @JsonProperty("user")
    private SimpleUserDto commentUserDto;


    public SimpleMemberDto(Member member) {
        this.id = member.getId();
        this.role = member.getRole().getName();
        this.commentUserDto = new SimpleUserDto(member.getUser());
    }



}
