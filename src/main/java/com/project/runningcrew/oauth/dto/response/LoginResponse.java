package com.project.runningcrew.oauth.dto.response;

import com.project.runningcrew.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private boolean initData;

}
