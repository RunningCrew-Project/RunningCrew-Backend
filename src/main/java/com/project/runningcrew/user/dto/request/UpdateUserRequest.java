package com.project.runningcrew.user.dto.request;

import com.project.runningcrew.user.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateUserRequest {


    //note 필수 값

    @Schema(description = "수정 닉네임 입력", example = "change_nickname")
    @NotBlank(message = "수정할 닉네임을 입력해주세요.")
    private String nickname;

    @Schema(description = "수정할 전화번호", example = "010-1234-5678")
    @NotBlank(message = "전화번호 값은 필수입니다.")
    private String phoneNumber;

    @Schema(description = "수정 비밀번호 입력", example = "same_password")
    @NotBlank(message = "수정할 비밀번호 값을 입력해주세요.")
    private String password;

    @Schema(description = "수정 비밀번호 재입력", example = "same_password")
    @NotBlank(message = "수정할 비밀번호 값을 재입력해주세요.")
    private String passwordCheck;

    @Schema(description = "수정 동네 아이디", example = "1")
    @NotNull(message = "동네 아이디는 필수 값입니다.")
    @Positive(message = "동네 아이디는 1 이상의 수입니다.")
    private Long dongId;




    //note 필수 아닌 값

    @Schema(description = "수정할 프로필 이미지", example = "imgUrl")
    private MultipartFile file;

    @Schema(description = "수정 생년월일 정보", example = "2022-02-22")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "수정 성별 정보", example = "MAN")
    private Sex sex;

    @Schema(description = "수정 신장 정보", example = "180")
    private Integer height;

    @Schema(description = "수정 몸무게 정보", example = "80")
    private Integer weight;


}
