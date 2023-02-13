package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RecruitAnswer;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class RecruitAnswerRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CrewRepository crewRepository;
    @Autowired private RecruitAnswerRepository recruitAnswerRepository;


    public User testUser() {
        User user = User.builder()
                .email("email@email.com")
                .password("password123!")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        return userRepository.save(user);
    }

    public Crew testCrew() {
        Crew crew = Crew.builder()
                .name("name")
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();
        return crewRepository.save(crew);
    }



    @DisplayName("RecruitAnswer save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        String answer = "answer";
        int offset = 123;
        RecruitAnswer recruitAnswer = new RecruitAnswer(testUser(), testCrew(), answer, offset);

        //when
        RecruitAnswer findRecruitAnswer = recruitAnswerRepository.save(recruitAnswer);

        //then
        Assertions.assertThat(recruitAnswer).isEqualTo(findRecruitAnswer);
    }

    @DisplayName("RecruitAnswer findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        String answer = "answer";
        int offset = 123;
        RecruitAnswer savedRecruitAnswer = recruitAnswerRepository.save(new RecruitAnswer(testUser(), testCrew(), answer, offset));

        //when
        Optional<RecruitAnswer> findRecruitAnswerOpt = recruitAnswerRepository.findById(savedRecruitAnswer.getId());

        //then
        Assertions.assertThat(findRecruitAnswerOpt).isNotEmpty();
        Assertions.assertThat(findRecruitAnswerOpt).hasValue(savedRecruitAnswer);
    }

    @DisplayName("RecruitAnswer delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        String answer = "answer";
        int offset = 123;
        RecruitAnswer savedRecruitAnswer = recruitAnswerRepository.save(new RecruitAnswer(testUser(), testCrew(), answer, offset));

        //when
        recruitAnswerRepository.delete(savedRecruitAnswer);

        //then
        Optional<RecruitAnswer> findRecruitAnswerOpt = recruitAnswerRepository.findById(savedRecruitAnswer.getId());
        Assertions.assertThat(findRecruitAnswerOpt).isEmpty();
    }

    @DisplayName("특정 UserId 를 가진 RecruitAnswer offset 정렬후 출력 테스트")
    @Test
    void findAllByUserAndCrewTest() throws Exception {
        //given
        User user = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .location("location")
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );

        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .location("location")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .build()
        );

        String answer = "answer";
        for (int i = 10; i >= 0; i--) { // 10 -> 0
            recruitAnswerRepository.save(new RecruitAnswer(user, crew, answer, i));
            // offset save [10, 0]
        }

        //when
        List<RecruitAnswer> findRecruitAnswerList = recruitAnswerRepository.findAllByUserAndCrew(user, crew);

        //then
        for(int i = 0; i <= 10; i++) {
            Assertions.assertThat(findRecruitAnswerList.get(i).getAnswerOffset()).isEqualTo(i);
            // [0, 10] 정렬 여부 확인
        }
    }



    @DisplayName("특정 Crew 의 RecruitAnswer 를 작성한 User 반환 테스트")
    @Test
    @Rollback(value = false)
    void findUserByRecruitAnswerTest() throws Exception {
        //given
        User user = testUser();
        Crew crew = testCrew();
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 1));
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 2));
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 3));

        //when
        List<User> findUserList = recruitAnswerRepository.findUserByRecruitAnswer(crew);

        //then
        Assertions.assertThat(findUserList.size()).isEqualTo(1);
        Assertions.assertThat(findUserList.get(0)).isEqualTo(user);
    }


}