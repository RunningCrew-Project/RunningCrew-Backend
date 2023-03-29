package com.project.runningcrew.common.dto;

import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SimpleUserDto {

    @Schema(description = "유저 계정 아이디", example = "1")
    private Long id;

    @Schema(description = "유저 계정 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "유저 계정 이미지", example = "imgUrl")
    private String imgUrl;

    public SimpleUserDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.imgUrl = user.getImgUrl();
    }

}
