package com.project.runningcrew.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleMemberDto {

    @Schema(description = "작성자 아이디", example = "1")
    private Long id;

    @Schema(description = "작성자 권한", example = "ROLE_NORMAL")
    private MemberRole memberRole;

    @Schema(description = "작성자 계정 정보")
    @JsonProperty("user")
    private SimpleUserDto commentUserDto;


    public SimpleMemberDto(Member member) {
        this.id = member.getId();
        this.memberRole = member.getRole();
        this.commentUserDto = new SimpleUserDto(member.getUser());
    }



}
