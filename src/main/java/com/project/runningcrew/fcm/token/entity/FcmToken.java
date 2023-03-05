package com.project.runningcrew.fcm.token.entity;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Table(name = "fcm_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "fcm_token_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "토큰값은 필수값입니다.")
    @Column(nullable = false)
    private String fcmToken;

    public FcmToken(User user, String fcmToken) {
        this.user = user;
        this.fcmToken = fcmToken;
    }

    public FcmToken(Long id, User user, String fcmToken) {
        this.id = id;
        this.user = user;
        this.fcmToken = fcmToken;
    }

}
