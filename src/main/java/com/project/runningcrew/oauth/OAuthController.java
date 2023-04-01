package com.project.runningcrew.oauth;


import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exception.PasswordCheckFailException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.oauth.dto.request.SignUpDto;
import com.project.runningcrew.oauth.dto.response.LoginResponse;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.user.dto.request.CheckNicknameRequest;
import com.project.runningcrew.user.dto.request.CreateUserRequest;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.service.UserService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;
    private final UserService userService;
    private final DongAreaService dongAreaService;



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


    /**
     *
     * 3가지 소셜로그인으로 가져올 수 있는 공통 정보는 이메일 밖에 없다.
     * Apple 의 경우 실사용자 이름, 프로필 이미지가 API 정보에 포함되지 않음.
     * 따라서 나머지 정보는 회원가입 직후 추가 정보입력을 받는 방안이 필요함.
     *
     * 그런데 기존에는 회원가입을 UserService 에서 userService.saveNormalUser(user, createUserRequest.getFile()); 를 사용했는데
     * 소셜로그인의 경우 OAuth 단계에서 회원가입이 안되어있으면 바로 등록하고 User 를 반환해야함.
     * userService.saveNormalUser(user, createUserRequest.getFile()) 처럼 프로필 이미지와 함께 유저를 저장할 수 없을듯
     *
     *
     */


    @Operation(
            summary = "회원가입 직후 추가정보 입력받기",
            description = "유저 추가정보를 입력받은 후 저장한다." ,
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createUser(
            @ModelAttribute @Valid SignUpDto signUpDto,
            @Parameter(hidden = true) @CurrentUser User user
    ) {

        //note 필수 추가정보
        String name = signUpDto.getName();
        user.updateName(name);

        String nickname = signUpDto.getNickname();
        user.updateNickname(nickname);

        MultipartFile file = signUpDto.getFile();
        user.updateImgUrl(String.valueOf(file));

        DongArea dongArea = dongAreaService.findById(signUpDto.getDongId());
        user.updateDongArea(dongArea);


        //note 필수 추가정보 X
        if (signUpDto.getSex() != null) {
            user.updateSex(signUpDto.getSex());
        }

        if (signUpDto.getBirthday() != null) {
            user.updateBirthday(signUpDto.getBirthday());
        }

        if (signUpDto.getHeight() != null) {
            user.updateHeight(signUpDto.getHeight());
        }

        if (signUpDto.getWeight() != null) {
            user.updateWeight(signUpDto.getWeight());
        }

        /**
        Long userId = userService.saveNormalUser(user, createUserRequest.getFile());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/users/{id}")
                .build(userId);

        return ResponseEntity.created(uri).build();
         */
        return null;

    }



}
