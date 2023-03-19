package com.project.runningcrew.runningnotice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RunningNoticeListResponse<T> {

    @Schema(description = "런닝 공지 리스트")
    private List<T> runningNotices;

}
