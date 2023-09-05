package com.project.runningcrew.user.repository;

import com.project.runningcrew.user.entity.User;

import java.util.Optional;

public interface UserJdbcRepository {

    Optional<User> findByIdForAdmin(Long id);

    void deleteForAdmin(User user);

}
