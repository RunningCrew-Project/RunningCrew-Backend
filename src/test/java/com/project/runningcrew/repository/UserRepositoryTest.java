package com.project.runningcrew.repository;

import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @DisplayName("User save 테스트")
    @Test
    public void saveTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name1")
                .nickname("nickname1")
                .imgUrl("imgUrl1")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber1")
                .location("location1")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        //when
        User savedUser = userRepository.save(user1);
        //then
        Assertions.assertThat(savedUser).isEqualTo(savedUser);

    }


    @DisplayName("User findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name1")
                .nickname("nickname1")
                .imgUrl("imgUrl1")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber1")
                .location("location1")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        User savedUser = userRepository.save(user1);
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
        User user1 = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name1")
                .nickname("nickname1")
                .imgUrl("imgUrl1")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber1")
                .location("location1")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        User savedUser = userRepository.save(user1);
        Long savedUserId = savedUser.getId();
        //when
        userRepository.delete(savedUser);
        //then
        Optional<User> findMemberOpt = userRepository.findById(savedUserId);
        Assertions.assertThat(findMemberOpt).isEmpty();
    }


}