package com.project.runningcrew.resourceimage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageListResponse {

    @Schema(description = "이미지 리스트")
    List<SimpleImageDto> images;

}
