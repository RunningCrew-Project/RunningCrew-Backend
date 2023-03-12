package com.project.runningcrew.area.dto;

import com.project.runningcrew.area.entity.GuArea;
import lombok.Getter;

@Getter
public class SimpleGuDto {

    private Long id;
    private String name;

    public SimpleGuDto(GuArea guArea) {
        this.id = guArea.getId();
        this.name = guArea.getName();
    }

}
