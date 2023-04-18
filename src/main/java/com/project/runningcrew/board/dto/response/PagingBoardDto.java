package com.project.runningcrew.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.common.dto.SimpleBoardDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PagingBoardDto {

    @Schema(description = "게시글 아이디", example = "1")
    private Long id;

    @Schema(description = "게시글 생성 시간", example = "2023-02-25 11:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "게시글 제목", example = "title")
    private String title;

    @Schema(description = "게시글 썸네일")
    private String imgUrl;

    @Schema(description = "게시글 작성자 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "게시글 댓글 수", example = "3")
    private Long commentCount;

    public PagingBoardDto(SimpleBoardDto simpleBoardDto, String imgUrl, Long commentCount) {
        this.id = simpleBoardDto.getId();
        this.createdDate = simpleBoardDto.getCreatedDate();
        this.title = simpleBoardDto.getTitle();
        this.imgUrl = imgUrl;
        this.nickname = simpleBoardDto.getNickname();
        this.commentCount = commentCount;
    }


}
