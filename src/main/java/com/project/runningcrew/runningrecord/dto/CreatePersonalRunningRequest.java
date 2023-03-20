package com.project.runningcrew.runningrecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreatePersonalRunningRequest {

    @Past
    @NotNull
    @Schema(description = "런닝 시작 시간", example = "2023-03-03 11:11")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @Schema(description = "런닝 장소", example = "서울특별시 성동구")
    @Pattern(regexp = "([가-힣A-Za-z]+(시|도)\\s[가-힣A-Za-z]+(시|군|구)+)",
            message = "올바르지 않은 형식의 이름입니다.")
    private String location;

    @PositiveOrZero(message = "런닝 거리는 0 이상입니다.")
    @Schema(description = "런닝 거리(km)", example = "3.1")
    private double runningDistance;

    @Positive(message = "런닝 시간은 1 이상입니다.")
    @Schema(description = "런닝 시간(초)", example = "2400")
    private int runningTime;

    @Positive(message = "런닝 페이스는 1 이상입니다.")
    @Schema(description = "런닝 페이스(초)", example = "400")
    private int runningFace;

    @Positive(message = "런닝 칼로리는 1 이상입니다.")
    @Schema(description = "소모 칼로리", example = "300")
    private int calories;

    @Size(max = 500, message = "런닝 기록 내용은 500 자 이하입니다.")
    @Schema(description = "기록 내용", example = "detail")
    private String runningDetails;

    @NotNull
    @Schema(description = "gps 경로")
    private List<GpsDto> gps;

    @Schema(description = "이미지 파일")
    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        if (files == null) {
            return Collections.emptyList();
        }
        return files;
    }

}
