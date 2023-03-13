package com.project.runningcrew.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class ChangeCommentRequest {

    @NotBlank
    @Size(min = 1, max = 200, message = "댓글 내용은 1자 이상 200자 이하입니다.")
    @Schema(description = "수정을 원하는 댓글 내용", example = "new_detail")
    private String detail;

}
