package com.project.runningcrew.user.dto.request;


import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CreateUserRequest {


    //note 필수 값

    @Email
    @Schema(description = "생성 유저 이메일", example = "example@gmail.com")
    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;

    @Schema(description = "생성 유저 이름", example = "전용수")
    @NotBlank(message = "이름은 필수 값입니다.")
    private String name;

    @Schema(description = "생성 유저 닉네임", example = "nickname")
    @NotBlank(message = "닉네임은 필수 값입니다.")
    private String nickname;

    @Schema(description = "생성 유저 프로필 이미지")
    @NotNull(message = "프로필 이미지는 필수 값입니다.")
    private MultipartFile file;

    @Schema(description = "생성 유저 휴대폰 번호", example = "01012345678")
    @NotBlank(message = "휴대폰 번호는 필수 값입니다.")
    private String phoneNumber;

    @Schema(description = "생성 유저 비밀번호 입력", example = "same_password")
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;

    @Schema(description = "생성 유저 비밀번호 재입력", example = "same_password")
    @NotBlank(message = "비밀번호 재입력은 필수 값입니다.")
    private String passwordCheck;

    @Schema(description = "생성 유저 동네 아이디", example = "1")
    @NotNull(message = "동네 아이디는 필수 값입니다.")
    @Positive(message = "동네 아이디는 1 이상의 수입니다.")
    private Long dongId;

    @Schema(description = "생성 유저 로그인 타입", example = "EMAIL")
    private LoginType loginType;



    //note 필수 아닌 값

    @Schema(description = "생성 유저 생년월일", example = "1998-08-26")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "생성 유저 성별", example = "MAN")
    private Sex sex;

    @Schema(description = "생성 유저 신장", example = "180")
    private Integer height;

    @Schema(description = "생성 유저 몸무게", example = "80")
    private Integer weight;


}
