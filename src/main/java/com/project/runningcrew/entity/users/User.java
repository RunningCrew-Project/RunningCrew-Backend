package com.project.runningcrew.entity.users;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.members.Member;
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

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Email
    @NotBlank(message = "유저 이메일은 필수값입니다.")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "이름은 필수값입니다.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "닉네임은 필수값입니다.")
    @Column(unique = true, nullable = false)
    private String nickname;

    @NotBlank(message = "이미지는 필수값입니다.")
    @Column(nullable = false)
    private String imgUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType login_type;

    @NotBlank(message = "핸드폰 번호는 필수값입니다.")
    @Column(nullable = false)
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,16}$",
            message = "비밀번호는 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.")
    @NotNull
    private String password;

    @NotNull
    @Column(nullable = false)
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private LocalDate birthday;

    @PositiveOrZero(message = "키는 0 이상입니다.")
    @Column(nullable = false)
    private int height;

    @PositiveOrZero(message = "몸무게는 0 이상입니다.")
    @Column(nullable = false)
    private int weight;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Member> members = new ArrayList<>();

    public static User createBasicUser(String email, String name, String nickname, String imgUrl,
                                  LoginType login_type, String phoneNumber) {
        return User.builder().email(email)
                .name(name)
                .nickname(nickname)
                .imgUrl(imgUrl)
                .login_type(login_type)
                .phoneNumber(phoneNumber)
                .location("")
                .sex(Sex.MAN)
                .birthday(LocalDate.of(1990,1,1))
                .height(170)
                .weight(70)
                .build();
    }

    @Builder
    public User(String email, String password, String name, String nickname, String imgUrl,
                LoginType login_type, String phoneNumber, String location, Sex sex,
                LocalDate birthday, int height, int weight) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.login_type = login_type;
        this.phoneNumber = phoneNumber;
        this.location = location;
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

    public void updateLocation(String location) {
        this.location = location;
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
