package com.project.runningcrew.blocked.reported.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetReportedCommentResponse {

    private Long reportId;

    private Long commentId;

    private Long reporterMemberId;

}
