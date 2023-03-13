package com.project.runningcrew.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class CreateRunningNoticeCommentRequest {

    @NotBlank
    @Size(min = 1, max = 200, message = "댓글 내용은 1자 이상 200자 이하입니다.")
    @Schema(description = "작성한 게시글 댓글 내용", example = "detail")
    private String detail;

}
