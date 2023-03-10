package com.project.runningcrew.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetCommentMemberDto {

    @Schema(description = "댓글 작성자 아이디", example = "1")
    private Long id;

    @Schema(description = "댓글 작성자 권한", example = "ROLE_NORMAL")
    private MemberRole memberRole;

    @Schema(description = "댓글 작성자 계정 정보")
    @JsonProperty("user")
    private GetCommentUserDto commentUserDto;


    public GetCommentMemberDto(Member member) {
        this.id = member.getId();
        this.memberRole = member.getRole();
        this.commentUserDto = new GetCommentUserDto(member.getUser());
    }



}
