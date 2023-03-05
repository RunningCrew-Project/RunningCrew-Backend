package com.project.runningcrew.notification.service;

import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.notification.entity.Notification;
import com.project.runningcrew.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 특정 User 의 Notification 을 페이징하여 반환한다.
     *
     * @param user
     * @param pageable
     * @return 특정 User 의 Notification
     */
    public Slice<Notification> findByUser(User user, Pageable pageable) {
        return notificationRepository.findByUser(user, pageable);
    }

}
