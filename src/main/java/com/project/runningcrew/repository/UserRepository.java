package com.project.runningcrew.repository;

import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
