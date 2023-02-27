package com.project.runningcrew.repository;

import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {




    //findByEmail -> Optional

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

}
