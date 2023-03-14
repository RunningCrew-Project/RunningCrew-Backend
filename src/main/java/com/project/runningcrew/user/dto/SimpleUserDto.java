package com.project.runningcrew.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleUserDto {

    @Schema(description = "유저 아이디", example = "1")
    private Long id;

    @Schema(description = "유저 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "유저 이미지 url", example = "imgUrl")
    private String imgUrl;

}
