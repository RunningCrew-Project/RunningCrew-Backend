package com.project.runningcrew.user.controller;

import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.common.annotation.CurrentUser;
import com.project.runningcrew.common.dto.SimpleUserDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.service.CrewService;
import com.project.runningcrew.exceptionhandler.ErrorResponse;
import com.project.runningcrew.member.service.MemberAuthorizationChecker;
import com.project.runningcrew.recruitanswer.service.RecruitAnswerService;
import com.project.runningcrew.user.dto.request.*;
import com.project.runningcrew.user.dto.request.change.UpdateUserRequest;
import com.project.runningcrew.user.dto.request.checkduplicate.CheckEmailRequest;
import com.project.runningcrew.user.dto.request.checkduplicate.CheckNicknameRequest;
import com.project.runningcrew.user.dto.response.GetMyDataResponse;
import com.project.runningcrew.user.dto.response.GetUserResponse;
import com.project.runningcrew.user.dto.response.UserListResponse;
import com.project.runningcrew.user.entity.LoginType;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "User", description = "user 에 관한 api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;
    private final DongAreaService dongAreaService;
    private final CrewService crewService;
    private final RecruitAnswerService recruitAnswerService;
    private final MemberAuthorizationChecker memberAuthorizationChecker;

    @Value("${domain.name}")
    private String host;


    @Operation(summary = "특정 유저 정보 가져오기",
            description = "유저 아이디에 맞는 유저의 정보를 가져온다.",
            security = {@SecurityRequirement(name = "Bearer-Key")})
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


    @Operation(summary = "유저 생성하기",
            description = "자체 회원가입 기능으로, 현재는 소셜 회원가입만을 제공합니다. \n" +
                    "기능테스트를 위해 남겨두었습니다. \n" +
                    "기존 필요 정보중 비밀번호, 전화번호 값을 삭제하였습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content()),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/api/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createUser(@ModelAttribute @Valid CreateUserRequest createUserRequest) {


        //note 필수 값 Build : [ 프로필 이미지 : 기본 값 자동 설정됨 ]
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .name(createUserRequest.getName())
                .nickname(createUserRequest.getNickname())
                .dongArea(dongAreaService.findById(createUserRequest.getDongId()))
                .login_type(LoginType.EMAIL)
                .build();



        //note 필수 아닌 값 Update
        if (createUserRequest.getSex() != null) {
            user.updateSex(createUserRequest.getSex());
        }
        if (createUserRequest.getBirthday() != null) {
            user.updateBirthday(createUserRequest.getBirthday());
        }
        if (createUserRequest.getHeight() != null) {
            user.updateHeight(createUserRequest.getHeight());
        }
        if (createUserRequest.getWeight() != null) {
            user.updateWeight(createUserRequest.getWeight());
        }

        //note Save
        Long userId = userService.saveNormalUser(user);

        URI uri = UriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/users/{id}")
                .build(userId);

        //note Return URL
        return ResponseEntity.created(uri).build();

    }


    @Operation(
            summary = "유저 정보 수정하기",
            description = "자체 로그인 기능을 삭제하고 소셜 로그인을 지원함으로써 \n" +
                    "비밀번호와 전화번호 정보가 사라졋습니다. \n" +
                    "유저 정보 수정시 필수 기입 정보는 : 이름, 닉네임, 동네 아이디, 프로필 이미지입니다 \n" +
                    "선택 기입 정보는 : [yyyy-MM-dd] 포맷의 생년월일, 성별, 키, 몸무게 정보입니다. \n",
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
    @PutMapping(value = "/api/users/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUser(
            @PathVariable("userId") Long userId,
            @ModelAttribute @Valid UpdateUserRequest updateUserRequest,
            @Parameter(hidden = true) @CurrentUser User user
    ) {

        //note 필수 값 Build
        User originUSer = userService.findById(userId);
        User updateUser = User.builder()
                .email(originUSer.getEmail()) // 변경 불가
                .name(updateUserRequest.getName())
                .nickname(updateUserRequest.getNickname())
                .dongArea(dongAreaService.findById(updateUserRequest.getDongId()))
                .login_type(originUSer.getLogin_type()) // 변경 불가
                .build();


        MultipartFile file = updateUserRequest.getFile();
        userService.updateUser(originUSer, updateUser, file);


        //note 필수 아닌 값 Update
        if (updateUserRequest.getSex() != null) {
            updateUser.updateSex(updateUserRequest.getSex());
        }
        if (updateUserRequest.getBirthday() != null) {
            updateUser.updateBirthday(updateUserRequest.getBirthday());
        }
        if (updateUserRequest.getHeight() != null) {
            updateUser.updateHeight(updateUserRequest.getHeight());
        }
        if (updateUserRequest.getWeight() != null) {
            updateUser.updateWeight(updateUserRequest.getWeight());
        }

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
    ) {
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


    @Operation(summary = "크루 가입신청한 유저 가져오기",
            description = "크루에 가입신청한 유저 정보를 가져온다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserListResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/crews/{crewId}/users")
    public ResponseEntity<UserListResponse> getUsersOfCrewApplier(
            @PathVariable("crewId") Long crewId,
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        Crew crew = crewService.findById(crewId);
        memberAuthorizationChecker.checkManager(user, crew);
        //note 요청 user 의 크루 매니저 권한 검증

        List<SimpleUserDto> dtoList = recruitAnswerService.findUserByCrew(crew).stream()
                .map(SimpleUserDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserListResponse(dtoList));
    }


    @Operation(summary = "로그인상태의 본인 계정 정보 가져오기",
            description = "현재 로그인중인 본인 계정 정보를 가져온다.",
            security = {@SecurityRequirement(name = "Bearer-Key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetMyDataResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/me")
    public ResponseEntity<GetMyDataResponse> getMyData(@Parameter(hidden = true) @CurrentUser User user) {
        return ResponseEntity.ok(new GetMyDataResponse(user));
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
