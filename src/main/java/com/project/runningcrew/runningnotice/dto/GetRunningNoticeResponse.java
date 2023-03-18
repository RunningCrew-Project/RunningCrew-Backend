package com.project.runningcrew.runningnotice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.common.dto.SimpleRunningRecordDto;
import com.project.runningcrew.member.dto.SimpleMemberDto;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetRunningNoticeResponse {

    @Schema(description = "런닝 공지 아이디", example = "1")
    private Long id;

    @Schema(description = "런닝 공지 일자", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "런닝 공지 제목", example = "title")
    private String title;

    @Schema(description = "런닝 공지 내용", example = "detail")
    private String detail;

    @Schema(description = "작성 멤버")
    private SimpleMemberDto member;

    @Schema(description = "런닝 공지 종류", example = "REGULAR")
    private String noticeType;

    @Schema(description = "런닝 참여 인원 수", example = "12")
    private long runningMemberCount;

    @Schema(description = "런닝 최대 인원 수", example = "30")
    private int runningPersonnel;

    @Schema(description = "런닝 시작 일자", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime runningDateTime;

    @Schema(description = "런닝 상태", example = "READY")
    private String status;

    @Schema(description = "첨부한 이전 런닝 기록")
    private SimpleRunningRecordDto preRunningRecord;

    public GetRunningNoticeResponse(RunningNotice runningNotice, long runningMemberCount) {
        this.id = runningNotice.getId();
        this.createdDate = runningNotice.getCreatedDate();
        this.title = runningNotice.getTitle();
        this.detail = runningNotice.getDetail();
        this.member = new SimpleMemberDto(runningNotice.getMember());
        this.noticeType = runningNotice.getNoticeType().toString();
        this.runningMemberCount = runningMemberCount;
        this.runningPersonnel = runningNotice.getRunningPersonnel();
        this.runningDateTime = runningNotice.getRunningDateTime();
        this.status = runningNotice.getStatus().toString();
        RunningRecord preRunningRecord = runningNotice.getPreRunningRecord();
        if (preRunningRecord != null) {
            this.preRunningRecord = new SimpleRunningRecordDto(preRunningRecord);
        }
    }

}
