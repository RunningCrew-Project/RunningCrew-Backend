package com.project.runningcrew.oauth;


import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.oauth.dto.request.SignUpDto;
import com.project.runningcrew.oauth.dto.response.LoginResponse;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/login/oauth")
    public ResponseEntity<LoginResponse> oauth2Login(@RequestBody OauthDto oauthDto) {
        LoginResponse loginResponse = oAuthService.socialLogin(oauthDto);
        return ResponseEntity.ok().body(loginResponse);
        //note : LoginResponse 에 추가정보 기입 유무 정보 포함.
    }



    @Operation(
            summary = "회원가입 직후 추가정보 입력받기",
            description = "유저 추가정보를 입력받은 후 저장한다." ,
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createUser(
            @ModelAttribute @Valid SignUpDto signUpDto,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        oAuthService.signUpData(user, signUpDto);
        return ResponseEntity.noContent().build();
    }







}
