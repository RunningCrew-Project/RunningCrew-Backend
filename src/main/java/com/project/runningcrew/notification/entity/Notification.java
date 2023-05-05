package com.project.runningcrew.notification.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @NotBlank(message = "알림 내용은 필수값입니다.")
    @Size(min = 1, max = 50, message = "알림 내용은 1 자 이상 50 자 이하입니다.")
    @Column(nullable = false, length = 50)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column
    private Long referenceId;

    public Notification(User user, Crew crew, String content, NotificationType type,
                         Long referenceId) {
        this.user = user;
        this.crew = crew;
        this.content = content;
        this.type = type;
        this.referenceId = referenceId;
    }

    public Notification(Long id, User user, Crew crew, String content,
                         NotificationType type, Long referenceId) {
        this.id = id;
        this.user = user;
        this.crew = crew;
        this.content = content;
        this.type = type;
        this.referenceId = referenceId;
    }

}
