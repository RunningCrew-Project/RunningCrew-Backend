package com.project.runningcrew.user.controller;

import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exception.PasswordCheckFailException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.user.dto.request.*;
import com.project.runningcrew.user.dto.response.GetUserResponse;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@Tag(description = "user 에 관한 api", name = "user")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;
    private final DongAreaService dongAreaService;

    @Value("${domain.name}")
    private String host;


    @Operation(summary = "유저 정보 가져오기", description = "유저 정보를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("userId") Long userId) {
        User findUser = userService.findById(userId);
        return ResponseEntity.ok(new GetUserResponse(findUser));
    }



    @Operation(summary = "유저 생성하기", description = "유저 정보를 생성한 후 저장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createUser(@ModelAttribute @Valid CreateUserRequest createUserRequest) {

        if(!createUserRequest.getPassword().equals(createUserRequest.getPasswordCheck())) {
            //note 비밀번호 & 비밀번호 재입력 Equal Check -> 일치하지 않으면 예외 발생
            throw new PasswordCheckFailException();
        }


        //note 필수 값 Build
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .name(createUserRequest.getName())
                .password(createUserRequest.getPassword())
                .nickname(createUserRequest.getNickname())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .dongArea(dongAreaService.findById(createUserRequest.getDongId()))
                .login_type(createUserRequest.getLoginType())
                .build();


        //note 필수 아닌 값 Update
        if(createUserRequest.getSex() != null) {
            user.updateSex(createUserRequest.getSex());
        }

        if(createUserRequest.getBirthday() != null) {
            user.updateBirthday(createUserRequest.getBirthday());
        }

        if(createUserRequest.getHeight() != null) {
            user.updateHeight(createUserRequest.getHeight());
        }

        if(createUserRequest.getWeight() != null) {
            user.updateWeight(createUserRequest.getWeight());
        }


        //note Save
        Long userId = userService.saveNormalUser(user, createUserRequest.getFile());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/users/{id}")
                .build(userId);

        //note Return URL
        return ResponseEntity.created(uri).build();

    }



    @Operation(summary = "유저 수정하기", description = "유저 정보를 수정한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
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
    @PutMapping(value = "/api/users/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUser(
            @PathVariable("userId") Long userId,
            @ModelAttribute @Valid UpdateUserRequest updateUserRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {

        if (!updateUserRequest.getPassword().equals(updateUserRequest.getPasswordCheck())) {
            //note 비밀번호 & 비밀번호 재입력 Equal Check -> 일치하지 않으면 예외 발생
            throw new PasswordCheckFailException();
        }

        User originUSer = userService.findById(userId);


        //note 필수 값 Build
        User updateUser = User.builder()
                .email(originUSer.getEmail()) // 변경 불가
                .name(originUSer.getName()) // 변경 불가
                .password(updateUserRequest.getPassword())
                .nickname(updateUserRequest.getNickname())
                .phoneNumber(updateUserRequest.getPhoneNumber())
                .dongArea(dongAreaService.findById(updateUserRequest.getDongId()))
                .login_type(originUSer.getLogin_type()) // 변경 불가
                .build();


        //note 필수 아닌 값 Update
        if(updateUserRequest.getSex() != null) {
            updateUser.updateSex(updateUserRequest.getSex());
        }

        if(updateUserRequest.getBirthday() != null) {
            updateUser.updateBirthday(updateUserRequest.getBirthday());
        }

        if(updateUserRequest.getHeight() != null) {
            updateUser.updateHeight(updateUserRequest.getHeight());
        }

        if(updateUserRequest.getWeight() != null) {
            updateUser.updateWeight(updateUserRequest.getWeight());
        }


        userService.updateUser(originUSer, updateUser, updateUserRequest.getFile());
        return ResponseEntity.noContent().build();

    }




    @Operation(summary = "유저 삭제하기", description = "유저 정보를 삭제한다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("userId") Long userId,
            @Parameter(hidden = true) @CurrentUser User user
    )
    {
        User findUser = userService.findById(userId);
        userService.deleteUser(findUser);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "유저 로그아웃", description = "유저 상태를 로그아웃으로 변경.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/logout")
    public ResponseEntity<Void> logoutUser(@Parameter(hidden = true) @CurrentUser User user) {

        log.info("current user={}", user);
        log.info("current userId={}", user.getId());
        userService.logOut(user);
        return ResponseEntity.noContent().build();
    }



    //TODO 크루 가입신청한 유저 가져오기


    //TODO 로그인 한 유저정보 가져오기




    @Operation(summary = "유저 이메일 중복체크하기", description = "유저 이메일 중복체크한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/users/duplicate/email")
    public ResponseEntity<Void> checkDuplicateEmail(@RequestBody @Valid CheckEmailRequest checkEmailRequest) {
        String email = checkEmailRequest.getEmail();
        userService.duplicateEmail(email);
        //note 이메일 중복 검증 -> 중복의 경우 409 Error
        return ResponseEntity.noContent().build();
        //note 중복 체크 통과 -> 204 No Content
    }

    @Operation(summary = "유저 닉네임 중복체크하기", description = "유저 닉네임 중복체크한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NO CONTENT", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/users/duplicate/nickname")
    public ResponseEntity<Void> checkDuplicateNickname(@RequestBody @Valid CheckNicknameRequest checkNicknameRequest) {
        String nickname = checkNicknameRequest.getNickname();
        userService.duplicateNickname(nickname);
        //note 이메일 중복 검증 -> 중복의 경우 409 Error
        return ResponseEntity.noContent().build();
        //note 중복 체크 통과 -> 204 No Content
    }



}
