package com.project.runningcrew.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginUserRequest {

    @Schema(description = "유저 로그인 이메일", example = "admin@naver.com")
    private String email;

    @Schema(description = "유저 로그인 비밀번호", example = "admin123!")
    private String password;

}
