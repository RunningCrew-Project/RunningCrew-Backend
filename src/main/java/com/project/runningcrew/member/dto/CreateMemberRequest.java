package com.project.runningcrew.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class CreateMemberRequest {

    @Schema(description = "멤버 생성할 유저의 id", example = "1")
    @Positive(message = "유저 id 는 1 이상의 수입니다.")
    private Long userId;

}
