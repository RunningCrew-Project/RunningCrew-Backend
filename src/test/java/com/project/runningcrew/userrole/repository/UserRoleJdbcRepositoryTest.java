package com.project.runningcrew.userrole.repository;

import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRoleJdbcRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    TestEntityFactory testEntityFactory;
    @Autowired
    EntityManager em;

    @Test
    public void findByUserForAdmin_테스트() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        UserRole userRole = new UserRole(user, Role.USER);
        userRole.updateDeleted(true);
        userRoleRepository.save(userRole);
        em.flush();
        em.clear();

        ///when
        Optional<UserRole> findUserRole = userRoleRepository.findByUserForAdmin(user);

        //then
        assertThat(findUserRole).isNotEmpty();
        assertThat(findUserRole.get().getId()).isEqualTo(userRole.getId());
    }

    @Test
    public void deleteForAdmin_테스트() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        UserRole userRole = new UserRole(user, Role.USER);
        userRoleRepository.save(userRole);
        em.flush();
        em.clear();

        ///when
        userRoleRepository.deleteForAdmin(userRole);

        //then
        Optional<UserRole> findUser = userRoleRepository.findByUser(user);
        assertThat(findUser).isEmpty();
    }

}