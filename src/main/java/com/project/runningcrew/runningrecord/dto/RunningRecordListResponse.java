package com.project.runningcrew.runningrecord.dto;

import com.project.runningcrew.common.dto.SimpleRunningRecordDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RunningRecordListResponse {

    @Schema(description = "런닝 기록 리스트")
    List<SimpleRunningRecordDto> runningRecords;

}
