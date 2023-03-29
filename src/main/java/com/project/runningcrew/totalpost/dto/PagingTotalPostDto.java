package com.project.runningcrew.totalpost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.totalpost.entity.TotalPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PagingTotalPostDto {

    @Schema(description = "글 id", example = "1")
    private Long id;

    @Schema(description = "글 생성 일자", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "글 제목", example = "title")
    private String title;

    @Schema(description = "작성자 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "글 종류", example = "BOARD")
    private String postType;

    @Schema(description = "글 이미지 url", example = "imgUrl")
    private String imgUrl;

    @Schema(description = "댓글 수", example = "30")
    private Long commentCount;

    public PagingTotalPostDto(TotalPost totalPost, String imgUrl, Long commentCount) {
        this.id = totalPost.getId();
        this.createdDate = totalPost.getCreatedDate();
        this.title = totalPost.getTitle();
        this.nickname = totalPost.getNickname();
        this.postType = totalPost.getPostType().toString();
        this.imgUrl = imgUrl;
        this.commentCount = commentCount;
    }

}
