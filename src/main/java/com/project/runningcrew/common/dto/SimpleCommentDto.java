package com.project.runningcrew.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.runningcrew.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleCommentDto {

    @Schema(description = "댓글 아이디", example = "1")
    private Long id;

    @Schema(description = "댓글 내용", example = "detail")
    private String detail;

    @Schema(description = "작성자 정보")
    @JsonProperty("member")
    private SimpleMemberDto simpleMemberDto;

    public SimpleCommentDto(Comment comment) {
        this.id = comment.getId();
        this.detail = comment.getDetail();
        this.simpleMemberDto = new SimpleMemberDto(comment.getMember());
    }


}
