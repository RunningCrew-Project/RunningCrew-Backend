package com.project.runningcrew.refreshtoken.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokensDto {

    private String accessToken;
    private String refreshToken;

    boolean isAccessToken() {
        return accessToken != null;
    }

    boolean isRefreshToken() {
        return refreshToken != null;
    }

}
