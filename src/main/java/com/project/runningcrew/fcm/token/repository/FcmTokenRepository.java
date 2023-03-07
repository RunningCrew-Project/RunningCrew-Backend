package com.project.runningcrew.fcm.token.repository;

import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    /**
     * 특정 User 의 FcmToken 을 찾아 반환. 없다면 Optional.empty() 반환
     *
     * @param user
     * @return 특정 User 의 FcmToken. 없다면 Optional.empty()
     */
    Optional<FcmToken> findByUser(@Param("user") User user);

    /**
     * 특정 User 를 포함하는 FcmToken 이 있다면 true, 없다면 false 반환
     *
     * @param user
     * @return 특정 User 를 포함하는 FcmToken 이 있다면 true, 없다면 false
     */
    boolean existsByUser(@Param("user") User user);

    /**
     * userId 의 리스트에 포함된 User 의 id 를 가지는 모든 FcmToken 을 찾아 반환
     *
     * @param userIds User 의 id 를 가지는 리스트
     * @return userId 의 리스트에 포함된 User 의 id 를 가지는 모든 FcmToken
     */
    @Query("select f from FcmToken f where f.user.id in (:userIds)")
    List<FcmToken> findAllByUserIds(@Param("userIds") List<Long> userIds);

}
