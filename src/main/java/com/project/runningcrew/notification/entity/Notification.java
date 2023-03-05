package com.project.runningcrew.notification.entity;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
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
    @GeneratedValue
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

    private Notification(User user, Crew crew, String content, NotificationType type,
                        Long referenceId) {
        this.user = user;
        this.crew = crew;
        this.content = content;
        this.type = type;
        this.referenceId = referenceId;
    }

    private Notification(Long id, User user, Crew crew, String content,
                        NotificationType type, Long referenceId) {
        this.id = id;
        this.user = user;
        this.crew = crew;
        this.content = content;
        this.type = type;
        this.referenceId = referenceId;
    }

    public static Notification createNoticeBoardNotification(User user, Crew crew, NoticeBoard board) {
        return new Notification(user, crew, board.getTitle(),
                NotificationType.NOTICE_BOARD, board.getId());
    }

    public static Notification createNoticeBoardNotification(Long id, User user, Crew crew,
                                                             NoticeBoard board) {
        return new Notification(id, user, crew, board.getTitle(),
                NotificationType.NOTICE_BOARD, board.getId());
    }

    public static Notification createRegularRunningNotification(User user, Crew crew, RunningNotice runningNotice) {
        if (runningNotice.getNoticeType() != NoticeType.REGULAR) {
            return null;
        }
        return new Notification(user, crew, runningNotice.getTitle(),
                NotificationType.REGULAR_RUNNING_NOTICE, runningNotice.getId());
    }

    public static Notification createRegularRunningNotification(Long id, User user, Crew crew,
                                                                RunningNotice runningNotice) {
        if (runningNotice.getNoticeType() != NoticeType.REGULAR) {
            return null;
        }
        return new Notification(id, user, crew, runningNotice.getTitle(),
                NotificationType.REGULAR_RUNNING_NOTICE, runningNotice.getId());
    }

}
