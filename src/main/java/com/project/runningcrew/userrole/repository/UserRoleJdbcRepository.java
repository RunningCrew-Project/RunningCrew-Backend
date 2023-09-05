package com.project.runningcrew.userrole.repository;

import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;

import java.util.Optional;

public interface UserRoleJdbcRepository {

    Optional<UserRole> findByUserForAdmin(User user);

    void deleteForAdmin(UserRole userRole);

}
