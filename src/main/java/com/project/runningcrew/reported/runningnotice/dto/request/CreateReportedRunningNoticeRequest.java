package com.project.runningcrew.reported.runningnotice.dto.request;

import com.project.runningcrew.reported.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CreateReportedRunningNoticeRequest {

    @Schema(description = "런닝 공지 신고 멤버의 아이디 값", example = "1")
    @NotNull(message = "런닝 공지 신고 멤버의 아이디 값은 필수입니다.")
    private Long reporterMemberId;

    @Schema(description = "신고된 런닝 공지의 아이디 값", example = "1")
    @NotNull(message = "런닝 공지 아이디 값은 필수입니다.")
    private Long runningNoticeId;

    @Schema(description = "런닝 공지 신고 사유", example = "ABUSE")
    @NotBlank(message = "런닝 공지 신고 사유 값은 필수입니다.")
    private ReportType reportType;

}
