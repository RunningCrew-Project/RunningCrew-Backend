package com.project.runningcrew.userrole.entity;

import com.project.runningcrew.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@SQLDelete(sql = "update user_roles set deleted = true where user_role_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "user_roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean deleted = false;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
