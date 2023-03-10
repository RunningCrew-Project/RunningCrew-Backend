package com.project.runningcrew.userrole.repository;

import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 특정 user 의 UserRole 을 반환. 없다면 Optional.empty() 반환
     *
     * @param user
     * @return 특정 user 의 UserRole. 없다면 Optional.empty()
     */
    Optional<UserRole> findByUser(User user);

}
