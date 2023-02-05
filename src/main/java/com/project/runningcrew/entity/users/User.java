package com.project.runningcrew.entity.users;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.members.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,16}$",
            message = "비밀번호는 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.")
    @NotNull
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType login_type;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int weight;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Member> members = new ArrayList<>();


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
