package com.project.runningcrew.user.repository;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TestEntityFactory testEntityFactory;

    @Autowired
    EntityManager em;


    @DisplayName("User save 테스트")
    @Test
    public void saveTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .dongArea(dongArea)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        //when
        User savedUser = userRepository.save(user);
        //then
        Assertions.assertThat(user).isEqualTo(savedUser);

    }


    @DisplayName("User findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User savedUser = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        //when
        Optional<User> findUserOpt = userRepository.findById(savedUser.getId());
        //then
        Assertions.assertThat(findUserOpt).isNotEmpty();
        Assertions.assertThat(findUserOpt).hasValue(savedUser);
    }


    @DisplayName("User delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User savedUser = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        //when
        userRepository.delete(savedUser);
        //then
        Optional<User> findMemberOpt = userRepository.findById(savedUser.getId());
        Assertions.assertThat(findMemberOpt).isEmpty();
    }


    @Test
    public void findByIdForAdmin_테스트() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .dongArea(dongArea)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        user.updateDeleted(true);
        userRepository.save(user);
        em.flush();
        em.clear();

        ///when
        Optional<User> findUser = userRepository.findByIdForAdmin(user.getId());

        //then
        Assertions.assertThat(findUser).isNotEmpty();
        Assertions.assertThat(findUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    public void findByEmailForAdmin_테스트() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        String email = "email@email.com";
        User user = User.builder()
                .email(email)
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .dongArea(dongArea)
                .build();
        user.updateDeleted(true);
        userRepository.save(user);
        em.flush();
        em.clear();

        ///when
        Optional<User> findUser = userRepository.findByEmailForAdmin(email);

        //then
        Assertions.assertThat(findUser).isNotEmpty();
        Assertions.assertThat(findUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void deleteForAdmin_테스트() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .dongArea(dongArea)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        userRepository.save(user);
        em.flush();
        em.clear();

        ///when
        userRepository.deleteForAdmin(user);

        //then
        Optional<User> findUser = userRepository.findByIdForAdmin(user.getId());
        Assertions.assertThat(findUser).isEmpty();
    }


}