package com.project.runningcrew.reported.comment.dto.response;

import com.project.runningcrew.reported.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetReportedCommentResponse {

    private Long reportId;

    private Long commentId;

    private Long reporterMemberId;

    private ReportType reportType;

}
