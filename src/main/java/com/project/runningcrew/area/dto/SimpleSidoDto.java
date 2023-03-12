package com.project.runningcrew.area.dto;

import com.project.runningcrew.area.entity.SidoArea;
import lombok.Getter;

@Getter
public class SimpleSidoDto {

    private Long id;
    private String name;

    public SimpleSidoDto(SidoArea sidoArea) {
        this.id = sidoArea.getId();
        this.name = sidoArea.getName();
    }

}
