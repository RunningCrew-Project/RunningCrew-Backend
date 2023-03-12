package com.project.runningcrew.area.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AreaListResponse<T> {

    private List<T> areas;

}
