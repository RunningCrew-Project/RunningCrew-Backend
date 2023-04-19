package com.project.runningcrew.reported.totalpost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetReportedTotalPostResponse {

    private Long reportId;

    private Long reportedPostId;

    private String reportedPostType;

    private String reportType;

}
