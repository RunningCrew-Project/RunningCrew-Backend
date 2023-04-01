package com.project.runningcrew.user.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {


    //note 필수 값

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Email
    @NotBlank(message = "유저 이메일은 필수값입니다.")
    @Column(unique = true, nullable = false)
    private String email;

    //@NotBlank(message = "이름은 필수값입니다.")
    //@Column(nullable = false)
    private String name;

    //@NotBlank(message = "닉네임은 필수값입니다.")
    //@Column(unique = true, nullable = false)
    private String nickname;

    //@NotBlank(message = "이미지는 필수값입니다.")
    //@Column(nullable = false)
    private String imgUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType login_type;

    //@NotBlank(message = "핸드폰 번호는 필수값입니다.")
    //@Column(nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_area_id")
    private DongArea dongArea;

    @NotNull
    private String password;




    //note 필수 아닌 값

    @Enumerated(EnumType.STRING)
    @Column()
    private Sex sex;

    @Column()
    private LocalDate birthday;

    @PositiveOrZero(message = "키는 0 이상입니다.")
    @Column()
    private Integer height;

    @PositiveOrZero(message = "몸무게는 0 이상입니다.")
    @Column()
    private Integer weight;

    public static User createBasicUser(String email, String name, String nickname, String imgUrl,
                                  LoginType login_type, String phoneNumber) {
        return User.builder().email(email)
                .name(name)
                .nickname(nickname)
                .imgUrl(imgUrl)
                .login_type(login_type)
                .phoneNumber(phoneNumber)
                .dongArea(null)
                .sex(Sex.MAN)
                .birthday(LocalDate.of(1990,1,1))
                .height(0)
                .weight(0)
                .build();
    }

    @Builder
    public User(Long id, String email, String password, String name, String nickname, String imgUrl,
                LoginType login_type, String phoneNumber, DongArea dongArea, Sex sex,
                LocalDate birthday, int height, int weight) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.login_type = login_type;
        this.phoneNumber = phoneNumber;
        this.dongArea = dongArea;
        this.sex = sex;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;

    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void updateDongArea(DongArea dongArea) {
        this.dongArea = dongArea;
    }

    public void updateSex(Sex sex) {
        this.sex = sex;
    }

    public void updateBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void updateHeight(int height) {
        this.height = height;
    }

    public void updateWeight(int weight) {
        this.weight = weight;
    }

}
