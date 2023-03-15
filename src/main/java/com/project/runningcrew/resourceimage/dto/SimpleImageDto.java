package com.project.runningcrew.resourceimage.dto;

import com.project.runningcrew.resourceimage.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SimpleImageDto {

    @Schema(description = "이미지 아이디", example = "1")
    private Long id;

    @Schema(description = "이미지 url", example = "imgUrl")
    private String imgUrl;

    public SimpleImageDto(Image image) {
        this.id = image.getId();
        this.imgUrl = image.getFileName();
    }

}
