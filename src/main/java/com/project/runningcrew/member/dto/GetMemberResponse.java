package com.project.runningcrew.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.common.dto.SimpleUserDto;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetMemberResponse {

    @Schema(description = "멤버 아이디", example = "2")
    private Long id;

    @Schema(description = "멤버 가입 일자", example = "2023-03-03")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "멤버의 유저")
    private SimpleUserDto user;

    @Schema(description = "멤버 권한", example = "MEMBER_NORMAL")
    private MemberRole role;

    public GetMemberResponse(Member member) {
        this.id = member.getId();
        this.createdDate = member.getCreatedDate();
        this.user = new SimpleUserDto(member.getUser());
        this.role = member.getRole();
    }

}
