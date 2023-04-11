package com.project.runningcrew.runningnotice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.member.dto.SimpleMemberDto;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PagingRunningNoticeDto {

    @Schema(description = "런닝공지 id", example = "1")
    private  Long id;

    @Schema(description = "런닝공지 생성 일자", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "런닝공지 제목", example = "title")
    private String title;

    @Schema(description = "런닝공지 이미지 url", example = "imgUrl")
    private String imgUrl;

    @Schema(description = "런닝공지 작성자 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "런닝공지 댓글 수", example = "1")
    private Long commentCount;

    public PagingRunningNoticeDto(NoticeWithUserDto notice, String imgUrl, Long commentCount) {
        this.id = notice.getId();
        this.createdDate = notice.getCreatedDate();
        this.title = notice.getTitle();
        this.nickname = notice.getNickname();
        this.imgUrl = imgUrl;
        this.commentCount = commentCount;
    }

}
