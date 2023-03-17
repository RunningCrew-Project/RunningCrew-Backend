package com.project.runningcrew.runningrecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GpsDto {

    @Schema(description = "위도", example = "37.5703")
    private double latitude;

    @Schema(description = "경도", example = "127.0741")
    private double longitude;

    @Override
    public String toString() {
        return "{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
