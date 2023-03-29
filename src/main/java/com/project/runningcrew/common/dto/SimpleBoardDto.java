package com.project.runningcrew.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
public class SimpleBoardDto {

    @Schema(description = "게시글 아이디", example = "1")
    private Long id;

    @Schema(description = "게시글 생성 시간", example = "2023-02-25 11:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "게시글 제목", example = "title")
    private String title;

    @Schema(description = "게시글 썸네일")
    private String imgUrl;

    @Schema(description = "게시글 작성자 정보")
    private SimpleMemberDto simpleMemberDto;

    @Schema(description = "게시글 댓글 수", example = "3")
    private Long commentCount;

    public SimpleBoardDto(Board board, String imgUrl, Long commentCount) {
        this.id = board.getId();
        this.createdDate = board.getCreatedDate();
        this.title = board.getTitle();
        this.imgUrl = imgUrl;
        this.simpleMemberDto = new SimpleMemberDto(board.getMember());
        this.commentCount = commentCount;

    }

}
