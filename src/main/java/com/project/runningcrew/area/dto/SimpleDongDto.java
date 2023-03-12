package com.project.runningcrew.area.dto;

import com.project.runningcrew.area.entity.DongArea;
import lombok.Getter;

@Getter
public class SimpleDongDto {

    private Long id;
    private String name;

    public SimpleDongDto(DongArea dongArea) {
        this.id = dongArea.getId();
        this.name = dongArea.getName();
    }

}
