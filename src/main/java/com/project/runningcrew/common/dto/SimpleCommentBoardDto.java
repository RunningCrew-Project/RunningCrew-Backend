package com.project.runningcrew.common.dto;

import com.project.runningcrew.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleCommentBoardDto {

    @Schema(description = "게시글 아이디", example = "1")
    private Long id;

    @Schema(description = "게시글 제목", example = "title")
    private String title;

    @Schema(description = "게시글 내용", example = "detail")
    private String detail;

    public SimpleCommentBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.detail = board.getDetail();
    }

}
