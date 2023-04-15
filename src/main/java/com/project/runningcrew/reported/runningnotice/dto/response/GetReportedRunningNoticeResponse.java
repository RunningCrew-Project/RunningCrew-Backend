package com.project.runningcrew.reported.runningnotice.dto.response;

import com.project.runningcrew.reported.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetReportedRunningNoticeResponse {

    private Long reportId;

    private Long runningNoticeId;

    private Long reporterMemberId;

    private ReportType reportType;

}
