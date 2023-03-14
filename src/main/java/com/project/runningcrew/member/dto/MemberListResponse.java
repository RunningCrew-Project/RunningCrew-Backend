package com.project.runningcrew.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberListResponse {

    @Schema(description = "멤버 수", example = "1")
    private int memberCount;

    @Schema(description = "멤버들의 리스트")
    private List<SimpleMemberDto> members;

}
