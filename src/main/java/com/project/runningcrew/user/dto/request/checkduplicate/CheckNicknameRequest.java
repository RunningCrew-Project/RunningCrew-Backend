package com.project.runningcrew.user.dto.request.checkduplicate;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class CheckNicknameRequest {

    @Schema(description = "중복 체크 닉네임", example = "nickname")
    @NotBlank(message = "닉네임은 필수값입니다.")
    @Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하입니다.")
    private String nickname;

}
