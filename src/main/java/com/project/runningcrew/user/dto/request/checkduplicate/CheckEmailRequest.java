package com.project.runningcrew.user.dto.request.checkduplicate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CheckEmailRequest {

    @Schema(description = "중복 체크 이메일", example = "example@gmail.com")
    @Email
    @NotBlank(message = "유저 이메일은 필수값입니다.")
    private String email;

}
