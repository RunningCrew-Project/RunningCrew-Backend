package com.project.runningcrew.runningnotice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RunningNoticeInfoDto {
    
    @Schema(description = "런닝 공지 아이디", example = "1")
    private Long id;

    @Schema(description = "런닝 공지 제목", example = "title")
    private String title;

    @Schema(description = "런닝 공지 종류", example = "REGULAR")
    private String noticeType;

    @Schema(description = "런닝 시작 일자", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime runningDateTime;
    
    @Schema(description = "런닝 참여 인원수", example = "24")
    private Long runningMemberCount;

    public RunningNoticeInfoDto(RunningNotice runningNotice, Long runningMemberCount) {
        this.id = runningNotice.getId();
        this.title = runningNotice.getTitle();
        this.noticeType = runningNotice.getNoticeType().toString();
        this.runningDateTime = runningNotice.getRunningDateTime();
        this.runningMemberCount = runningMemberCount;
    }

}
