package com.project.runningcrew.user.repository;

import com.project.runningcrew.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 입력받은 이메일로 유저를 찾아 반환한다.
     * @param email 이메일 정보
     * @return 유저 정보
     */
    Optional<User> findByEmail(String email);

    /**
     * 해당 이메일로 가입된 유저의 존재 여부를 반환한다.
     * @param email 이메일 정보
     * @return 가입 여부 정보
     */
    boolean existsByEmail(String email);

    /**
     * 해당 닉네임으로 가입된 유저의 존재 여부를 반환한다.
     * @param nickname 닉네임 정보
     * @return 가입 여부 정보
     */
    boolean existsByNickname(String nickname);

}
