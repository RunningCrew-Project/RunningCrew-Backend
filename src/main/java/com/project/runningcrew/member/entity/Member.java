package com.project.runningcrew.member.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@SQLDelete(sql = "update members set deleted = true where member_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column
    private boolean deleted = false;

    public Member(User user, Crew crew, MemberRole role) {
        this.user = user;
        this.crew = crew;
        this.role = role;
    }

    public Member(Long id, User user, Crew crew, MemberRole role) {
        this.id = id;
        this.user = user;
        this.crew = crew;
        this.role = role;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }

}
