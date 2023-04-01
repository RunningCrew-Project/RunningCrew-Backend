package com.project.runningcrew.oauth;


import com.project.runningcrew.oauth.dto.response.LoginResponse;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;
    @Operation(summary = "소셜 로그인",
            description = "AccessToken 을 body 에 넣어주세요." +
                    "\n 구글은 AccessToken, idToken, origin 까지 총 3개." +
                    "\n 네이버, 카카오는 AccessToken, origin 총 2개." +
                    "\n 애플은 idToken, origin 총 2개."
    )
    @PostMapping(value = "/api/login/oauth")
    public ResponseEntity<LoginResponse> oauth2Login(@RequestBody OauthDto oauthDto) {
        LoginResponse loginResponse = oAuthService.socialLogin(oauthDto);
        return ResponseEntity.ok().body(loginResponse);
    }


}
