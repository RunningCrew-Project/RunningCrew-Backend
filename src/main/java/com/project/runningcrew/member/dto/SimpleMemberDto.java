package com.project.runningcrew.member.dto;

import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.user.dto.SimpleUserDto;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class SimpleMemberDto {

    @Schema(description = "멤버 아이디", example = "2")
    private Long id;

    @Schema(description = "멤버의 유저", example = " {\n" +
            "    \"id\": 1,\n" +
            "    \"nickname\": \"admin\",\n" +
            "    \"imgUrl\": \"imgUrl\"\n" +
            "  }")
    private SimpleUserDto user;

    @Schema(description = "멤버 권한", example = "MEMBER_NORMAL")
    private MemberRole role;

    public SimpleMemberDto(Member member) {
        User user = member.getUser();
        this.id = member.getId();
        this.user = new SimpleUserDto(user.getId(), user.getNickname(), user.getImgUrl());
        this.role = member.getRole();
    }

}
