package com.project.runningcrew.runningrecord.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.runningcrew.runningrecord.entity.Gps;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetRunningRecordResponse {

    @Schema(description = "런닝 기록 id", example = "1")
    private Long id;

    @Schema(description = "런닝 기록 제목", example = "개인 런닝")
    private String title;

    @Schema(description = "런닝 시작 시간", example = "2023-03-03 11:11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDateTime;

    @Schema(description = "런닝 장소", example = "서울특별시 성동구")
    private String location;

    @Schema(description = "런닝 거리(km)", example = "3.1")
    private double runningDistance;

    @Schema(description = "런닝 시간(초)", example = "2400")
    private int runningTime;

    @Schema(description = "런닝 페이스(초)", example = "400")
    private int runningFace;

    @Schema(description = "소모 칼로리", example = "300")
    private int calories;

    @Schema(description = "기록 내용", example = "detail")
    private String runningDetail;

    @Schema(description = "gps 경로")
    private List<GpsDto> gps;

    public GetRunningRecordResponse(RunningRecord runningRecord) {
        this.id = runningRecord.getId();
        this.title = runningRecord.getTitle();
        this.startDateTime = runningRecord.getStartDateTime();
        this.location = runningRecord.getLocation();
        this.runningDistance = runningRecord.getRunningDistance();
        this.runningTime = runningRecord.getRunningTime();
        this.runningFace = runningRecord.getRunningFace();
        this.calories = runningRecord.getCalories();
        this.runningDetail = runningRecord.getRunningDetail();
        this.gps = runningRecord.getGpsList().stream()
                .sorted(Comparator.comparingInt(Gps::getGpsOffset))
                .map(g -> new GpsDto(g.getLatitude(), g.getLongitude()))
                .collect(Collectors.toList());
    }

}
