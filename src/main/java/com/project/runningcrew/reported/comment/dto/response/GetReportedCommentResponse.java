package com.project.runningcrew.reported.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetReportedCommentResponse {

    private Long reportId;

    private Long commentId;

    private Long reporterMemberId;

}
