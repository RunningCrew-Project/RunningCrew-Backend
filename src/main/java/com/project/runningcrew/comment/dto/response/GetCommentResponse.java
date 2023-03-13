package com.project.runningcrew.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.common.dto.SimpleCommentBoardDto;
import com.project.runningcrew.common.dto.SimpleMemberDto;
import com.project.runningcrew.common.dto.SimpleRunningNoticeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetCommentResponse {

    @Schema(description = "댓글 아이디", example = "1")
    private Long id;

    @Schema(description = "댓글 작성자 정보")
    @JsonProperty("member")
    private SimpleMemberDto simpleMemberDto;

    @Schema(description = "댓글 내용", example = "detail")
    private String detail;

    @Schema(description = "댓글이 작성된 게시글 정보")
    @JsonProperty("board")
    private SimpleCommentBoardDto simpleCommentBoardDto;

    @Schema(description = "댓글이 작성된 런닝 공지 정보")
    @JsonProperty("runningNotice")
    private SimpleRunningNoticeDto simpleRunningNoticeDto;

    public GetCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.simpleMemberDto = new SimpleMemberDto(comment.getMember());
        this.detail = comment.getDetail();

        // BoardComment or RunningNoticeComment
        if(comment instanceof BoardComment) {
            BoardComment boardComment = (BoardComment) comment;
            this.simpleCommentBoardDto = new SimpleCommentBoardDto(boardComment.getBoard());
            this.simpleRunningNoticeDto = null;
        } else if (comment instanceof RunningNoticeComment) {
            RunningNoticeComment runningNoticeComment = (RunningNoticeComment) comment;
            this.simpleRunningNoticeDto = new SimpleRunningNoticeDto(runningNoticeComment.getRunningNotice());
            this.simpleCommentBoardDto = null;
        }

    }




}


