package com.project.runningcrew.userrole.service;

import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    /**
     * 유저를 사용하여 유저 권한을 반환한다.
     *
     * @param user
     * @return 유저 권한
     * @throws UserRoleNotFoundException 유저 권한이 존재하지 않을 때
     */
    public UserRole findByUser(User user) {
        return userRoleRepository.findByUser(user).orElseThrow(UserRoleNotFoundException::new);
    }

}
