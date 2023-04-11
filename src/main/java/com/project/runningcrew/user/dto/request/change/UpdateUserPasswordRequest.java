package com.project.runningcrew.user.dto.request.change;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class UpdateUserPasswordRequest {

    @Schema(description = "수정 비밀번호 입력", example = "same_password")
    @NotBlank(message = "수정할 비밀번호 값을 입력해주세요.")
    private String password;

    @Schema(description = "수정 비밀번호 재입력", example = "same_password")
    @NotBlank(message = "수정할 비밀번호 값을 재입력해주세요.")
    private String passwordCheck;

}
