package com.project.runningcrew.user.dto.response;

import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetMyDataResponse {

    @Schema(description = "현재 로그인중인 본인 계정 아이디", example = "1")
    private Long id;

    @Schema(description = "현재 로그인중인 본인 계정 이메일", example = "example@email.com")
    private String email;

    @Schema(description = "현재 로그인중인 본인 계정 이름", example = "전용수")
    private String name;

    @Schema(description = "현재 로그인중인 본인 계정 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "현재 로그인중인 본인 계정 프로필 이미지 URL", example = "imgUrl")
    private String imgUrl;

    public GetMyDataResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.imgUrl = user.getImgUrl();
    }

}
