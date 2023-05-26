package com.project.runningcrew.user.dto.response;

import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetUserResponse {

    @Schema(description = "유저 아이디", example = "1")
    private Long id;

    @Schema(description = "유저 이메일", example = "example@gmail.com")
    private String email;

    @Schema(description = "유저 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "유저 프로필 이미지 url", example = "imgUrl")
    private String imgUrl;

    @Schema(description = "유저 로그인 타입", example = "EMAIL")
    private LoginType loginType;

    //@Schema(description = "유저 휴대폰 번호 정보", example = "01012345678")
    //private String phoneNumber;

    @Schema(description = "유저 성별 정보", example = "MAN")
    private Sex sex;

    @Schema(description = "유저 생년월일 정보", example = "1998-08-26")
    private LocalDate birthday;

    @Schema(description = "유저 신장 정보", example = "180")
    private int height;

    @Schema(description = "유저 몸무게 정보", example = "80")
    private int weight;

    public GetUserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.imgUrl = user.getImgUrl();
        this.loginType = user.getLogin_type();
        //this.phoneNumber = user.getPhoneNumber();
        this.sex = user.getSex();
        this.birthday = user.getBirthday();
        this.height = user.getHeight();
        this.weight = user.getWeight();
    }


}
