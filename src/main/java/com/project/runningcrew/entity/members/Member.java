package com.project.runningcrew.entity.members;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
