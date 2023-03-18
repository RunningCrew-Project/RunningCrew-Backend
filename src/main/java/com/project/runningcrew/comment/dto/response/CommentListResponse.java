package com.project.runningcrew.comment.dto.response;

import com.project.runningcrew.common.dto.SimpleCommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentListResponse {

    @Schema(description = "게시글의 댓글 수", example = "3")
    private int commentCount;

    @Schema(description = "댓글 리스트")
    private List<SimpleCommentDto> comments = new ArrayList<>();

}
