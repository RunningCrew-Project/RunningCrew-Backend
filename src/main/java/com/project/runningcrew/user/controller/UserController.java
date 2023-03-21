package com.project.runningcrew.user.controller;

import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.exception.PasswordCheckFailException;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.user.dto.request.CheckEmailRequest;
import com.project.runningcrew.user.dto.request.CheckNicknameRequest;
import com.project.runningcrew.user.dto.request.CreateUserRequest;
import com.project.runningcrew.user.dto.request.UpdateUserRequest;
import com.project.runningcrew.user.dto.response.GetUserResponse;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Tag(description = "user 에 관한 api", name = "user")
@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final DongAreaService dongAreaService;

    @Value("${domain.name}")
    private String host;


    @Operation(summary = "유저 정보 가져오기", description = "유저 정보를 가져온다.", security = {@SecurityRequirement(name = "Bearer-Key")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<GetUserResponse> getUser(
            @PathVariable("userId") Long userId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
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

        User user = User.builder()
                .email(createUserRequest.getEmail())
                .name(createUserRequest.getName())
                .password(createUserRequest.getPassword())
                .nickname(createUserRequest.getNickname())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .dongArea(dongAreaService.findById(createUserRequest.getDongId()))
                .login_type(createUserRequest.getLoginType())
                .sex(createUserRequest.getSex())
                .birthday(createUserRequest.getBirthday())
                .height(createUserRequest.getHeight())
                .weight(createUserRequest.getWeight())
                .build();

        Long userId = userService.saveNormalUser(user, createUserRequest.getFile());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/users/{id}")
                .build(userId);

        return ResponseEntity.created(uri).build();

    }



    @Operation(summary = "유저 수정하기", description = "유저 정보를 수정한다.")
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

        User updateUser = User.builder()
                .nickname(updateUserRequest.getNickname())
                .password(updateUserRequest.getPassword())
                .phoneNumber(updateUserRequest.getPhoneNumber())
                .dongArea(dongAreaService.findById(updateUserRequest.getDongId()))
                .sex(updateUserRequest.getSex())
                .birthday(updateUserRequest.getBirthday())
                .height(updateUserRequest.getHeight())
                .weight(updateUserRequest.getWeight())
                .build();

        userService.updateUser(userService.findById(userId), updateUser, updateUserRequest.getFile());
        return ResponseEntity.noContent().build();

    }



















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
