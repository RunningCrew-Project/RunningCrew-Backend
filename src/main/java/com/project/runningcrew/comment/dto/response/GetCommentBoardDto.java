package com.project.runningcrew.comment.dto.response;

import com.project.runningcrew.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetCommentBoardDto {

    @Schema(description = "댓글 게시물 아이디", example = "1")
    private Long id;

    @Schema(description = "댓글 게시물 제목", example = "title")
    private String title;

    @Schema(description = "댓글 게시글 내용", example = "detail")
    private String detail;

    public GetCommentBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.detail = board.getDetail();
    }

}
