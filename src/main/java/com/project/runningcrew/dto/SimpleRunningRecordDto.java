package com.project.runningcrew.dto;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SimpleRunningRecordDto {

    private Long runningRecordId;
    private String runningType;
    private LocalDateTime startDateTime;
    private double runningDistance;
    private int runningTime;
    private int runningFace;
    private String crewName;
    private String noticeType;

    public SimpleRunningRecordDto(BigInteger runningRecordId, String runningType, Timestamp startDateTime,
                                  Double runningDistance, Integer runningTime, Integer runningFace,
                                  String crewName, String noticeType) {
        this.runningRecordId = runningRecordId.longValue();
        this.runningType = runningType;
        this.startDateTime = startDateTime.toLocalDateTime();
        this.runningDistance = runningDistance;
        this.runningTime = runningTime;
        this.runningFace = runningFace;
        this.crewName = crewName;
        this.noticeType = noticeType;
    }

}
