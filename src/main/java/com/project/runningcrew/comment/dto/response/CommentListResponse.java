package com.project.runningcrew.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CommentListResponse<T> {

    @Schema(description = "게시글의 댓글 수", example = "3")
    private int commentCount;

    @Schema(description = "댓글 리스트")
    private List<T> comments = new ArrayList<>();

}
