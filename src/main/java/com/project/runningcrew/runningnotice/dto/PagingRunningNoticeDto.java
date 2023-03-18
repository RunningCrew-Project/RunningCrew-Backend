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

    @Schema(description = "런닝공지 내용", example = "detail")
    private String detail;

    @Schema(description = "런닝공지 이미지 url", example = "imgUrl")
    private String imgUrl;

    @Schema(description = "런닝공지 작성자")
    private SimpleMemberDto member;

    @Schema(description = "런닝 시작 일자", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime runningDateTime;

    @Schema(description = "런닝공지 종류", example = "REGULAR")
    private String noticeType;

    @Schema(description = "런닝 최대 인원", example = "30")
    private int runningPersonnel;

    @Schema(description = "런닝공지 댓글 수", example = "1")
    private int commentCount;

    public PagingRunningNoticeDto(RunningNotice runningNotice, String imgUrl, int commentCount) {
        this.id = runningNotice.getId();
        this.createdDate = runningNotice.getCreatedDate();
        this.title = runningNotice.getTitle();
        this.detail = runningNotice.getDetail();
        this.imgUrl = imgUrl;
        this.member = new SimpleMemberDto(runningNotice.getMember());
        this.runningDateTime = runningNotice.getRunningDateTime();
        this.noticeType = runningNotice.getNoticeType().toString();
        this.runningPersonnel = runningNotice.getRunningPersonnel();
        this.commentCount = commentCount;
    }

}
