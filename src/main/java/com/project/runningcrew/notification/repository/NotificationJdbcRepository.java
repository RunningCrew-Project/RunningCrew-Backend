package com.project.runningcrew.notification.repository;

import com.project.runningcrew.notification.entity.Notification;

import java.util.List;

public interface NotificationJdbcRepository {

    void saveAllCustom(List<Notification> notifications);

}
