package com.project.runningcrew.refreshtoken.repository;

import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 특정 User 의 RefreshToken 을 찾아서 반환. 없다면 Optional.empty() 반환
     *
     * @param user
     * @return 특정 User 의 RefreshToken. 없다면 Optional.empty()
     */
    Optional<RefreshToken> findByUser(User user);

}
