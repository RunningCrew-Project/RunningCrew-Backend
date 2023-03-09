package com.project.runningcrew.comment.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetCommentResponse {

    @Schema(description = "댓글 아이디", example = "1")
    private Long id;

    @Schema(description = "댓글 작성자 정보")
    @JsonProperty("member")
    private GetCommentMemberDto getCommentMemberDto;

    @Schema(description = "댓글 내용", example = "detail")
    private String detail;

    @Schema(description = "댓글이 작성된 게시글 정보")
    @JsonProperty("board")
    private GetCommentBoardDto getCommentBoardDto;

    @Schema(description = "댓글이 작성된 런닝 공지 정보")
    @JsonProperty("runningNotice")
    private GetCommentRunningNoticeDto getCommentRunningNoticeDto;

    public GetCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.getCommentMemberDto = new GetCommentMemberDto(comment.getMember());
        this.detail = comment.getDetail();

        // BoardComment or RunningNoticeComment
        if(comment instanceof BoardComment) {
            BoardComment boardComment = (BoardComment) comment;
            this.getCommentBoardDto = new GetCommentBoardDto(boardComment.getBoard());
            this.getCommentRunningNoticeDto = null;
        } else if (comment instanceof RunningNoticeComment) {
            RunningNoticeComment runningNoticeComment = (RunningNoticeComment) comment;
            this.getCommentRunningNoticeDto = new GetCommentRunningNoticeDto(runningNoticeComment.getRunningNotice());
            this.getCommentBoardDto = null;
        }

    }




}


