package com.project.runningcrew.comment.dto.response;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetCommentOfMemberResponse {

    @Schema(description = "댓글 아이디", example = "1")
    private Long id;

    @Schema(description = "댓글 내용", example = "detail")
    private String detail;

    @Schema(description = "댓글 타입", example = "board")
    private String type;

    @Schema(description = "참조 매개체 아이디", example = "1")
    private Long referenceId;

    public GetCommentOfMemberResponse(Comment comment) {
        this.id = comment.getId();
        this.detail = comment.getDetail();

        if(comment instanceof BoardComment) {
            this.type = "board";
            this.referenceId = ((BoardComment) comment).getBoard().getId();
        } else if (comment instanceof RunningNoticeComment) {
            this.type = "runningNotice";
            this.referenceId = ((RunningNoticeComment) comment).getRunningNotice().getId();
        }

    }


}
