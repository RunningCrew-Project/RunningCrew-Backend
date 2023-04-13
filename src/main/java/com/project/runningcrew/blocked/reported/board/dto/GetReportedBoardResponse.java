package com.project.runningcrew.blocked.reported.board.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetReportedBoardResponse {

    private Long reportId;

    private Long boardId;

    private Long reporterMemberId;

}
