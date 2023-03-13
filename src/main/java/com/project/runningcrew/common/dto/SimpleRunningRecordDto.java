package com.project.runningcrew.common.dto;

import com.project.runningcrew.runningrecord.entity.RunningRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SimpleRunningRecordDto {

    @Schema(description = "런닝 기록의 아이디", example = "1")
    private Long id;

    @Schema(description = "런닝 기록의 제목", example = "title")
    private String title;

    @Schema(description = "런닝 시작시간 기록 값", example = "2023-02-22 20:00:00")
    private LocalDateTime startDateTime;

    @Schema(description = "런닝 장소", example = "서울시 성동구")
    private String location;

    @Schema(description = "런닝 거리", example = "100.1")
    private double runningDistance;

    @Schema(description = "런닝 시간", example = "100")
    private int runningTime;

    @Schema(description = "런닝 페이스", example = "10")
    private int runningFace;


    public SimpleRunningRecordDto(RunningRecord runningRecord) {
        this.id = runningRecord.getId();
        this.title = runningRecord.getTitle();
        this.startDateTime = runningRecord.getStartDateTime();
        this.location = runningRecord.getLocation();
        this.runningDistance = runningRecord.getRunningDistance();
        this.runningTime = runningRecord.getRunningTime();
        this.runningFace = runningRecord.getRunningFace();
    }

}
