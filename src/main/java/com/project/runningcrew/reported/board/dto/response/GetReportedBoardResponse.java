package com.project.runningcrew.reported.board.dto.response;


import com.project.runningcrew.reported.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetReportedBoardResponse {

    private Long reportId;

    private Long boardId;

    private Long reporterMemberId;

    private ReportType reportType;

}
