package com.project.runningcrew.comment.dto.response;

import com.project.runningcrew.runningnotice.entity.RunningNotice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetCommentRunningNoticeDto {

    @Schema(description = "댓글 런닝 공지 아이디", example = "1")
    private Long id;

    @Schema(description = "댓글 런닝 공지 제목", example = "title")
    private String title;

    @Schema(description = "댓글 런닝 공지 내용", example = "detail")
    private String detail;

    public GetCommentRunningNoticeDto(RunningNotice runningNotice) {
        this.id = runningNotice.getId();
        this.title = runningNotice.getTitle();
        this.detail = runningNotice.getDetail();
    }

}
