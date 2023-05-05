package com.project.runningcrew.notification.entity;

import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;

public class NotificationFactory {

    public static Notification createNoticeBoardNotification(User user, Crew crew, NoticeBoard board) {
        return new Notification(user, crew, board.getTitle(),
                NotificationType.NOTICE_BOARD, board.getId());
    }

    public static Notification createNoticeBoardNotification(Long id, User user, Crew crew,
                                                             NoticeBoard board) {
        return new Notification(id, user, crew, board.getTitle(),
                NotificationType.NOTICE_BOARD, board.getId());
    }

    public static Notification createRegularRunningNotification(User user, Crew crew,
                                                                RunningNotice runningNotice) {
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
