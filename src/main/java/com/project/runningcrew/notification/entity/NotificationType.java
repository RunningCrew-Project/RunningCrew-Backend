package com.project.runningcrew.notification.entity;

public enum NotificationType {

    NOTICE_BOARD(NotificationTitle.NEW_NOTICE_BOARD_TITLE),
    REGULAR_RUNNING_NOTICE(NotificationTitle.NEW_REGULAR_RUNNING_NOTICE_TITLE),
    CREW(NotificationTitle.CREW_JOIN_TITLE);

    private String title;

    NotificationType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
