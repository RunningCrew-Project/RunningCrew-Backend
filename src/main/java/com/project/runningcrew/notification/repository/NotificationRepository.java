package com.project.runningcrew.notification.repository;

import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 특정 User 의 Notification 을 페이징하여 반환한다.
     *
     * @param user
     * @param pageable
     * @return 특정 User 의 Notification
     */
    @EntityGraph(attributePaths = {"user", "crew"})
    Slice<Notification> findByUser(@Param("user") User user, Pageable pageable);

}
