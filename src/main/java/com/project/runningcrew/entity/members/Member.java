package com.project.runningcrew.entity.members;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberRole role;

    public Member(User user, Crew crew, MemberRole role) {
        if (user != null) {
            setUser(user);
        }

        this.crew = crew;
        this.role = role;
    }

    private void setUser(User user) {
        this.user = user;
        user.getMembers().add(this);
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }

}
