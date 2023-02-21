package com.project.runningcrew.repository;

import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 입력된 nickname 을 사용중인 User List 를 반환한다.
     * @param nickname
     * @return nickname 을 사용중인 User list
     */
    List<User> findAllByNickname(String nickname);

    /**
     * 입력된 email 로 가입된 User List 를 반환한다.
     * @param email
     * @return email 로 가입한 User list
     */
    List<User> findAllByEmail(String email);

}
