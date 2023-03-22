package com.project.runningcrew.user.dto.response;

import com.project.runningcrew.common.dto.SimpleUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserListResponse {

    @Schema(description = "크루 가입 신청한 유저들의 정보")
    List<SimpleUserDto> users;

}
