package com.project.runningcrew.oauth.dto.response;

import com.project.runningcrew.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    User user;

    private String accessToken;

    private String refreshToken;

}
