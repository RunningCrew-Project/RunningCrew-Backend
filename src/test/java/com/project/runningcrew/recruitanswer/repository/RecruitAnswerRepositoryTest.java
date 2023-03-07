package com.project.runningcrew.recruitanswer.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class RecruitAnswerRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CrewRepository crewRepository;
    @Autowired private RecruitAnswerRepository recruitAnswerRepository;
    @Autowired
    TestEntityFactory testEntityFactory;


    public User testUser(DongArea dongArea) {
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
        return userRepository.save(user);
    }

    public Crew testCrew(DongArea dongArea) {
        Crew crew = Crew.builder()
                .name("name")
                .dongArea(dongArea)
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea);
        Crew crew = testCrew(dongArea);
        RecruitAnswer recruitAnswer = new RecruitAnswer(user, crew, answer, offset);

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
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea);
        Crew crew = testCrew(dongArea);
        RecruitAnswer savedRecruitAnswer = recruitAnswerRepository.save(new RecruitAnswer(user, crew, answer, offset));

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
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea);
        Crew crew = testCrew(dongArea);
        RecruitAnswer savedRecruitAnswer = recruitAnswerRepository.save(new RecruitAnswer(user, crew, answer, offset));

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
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = userRepository.save(
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

        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .dongArea(dongArea)
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea);
        Crew crew = testCrew(dongArea);
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 1));
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 2));
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 3));

        //when
        List<User> findUserList = recruitAnswerRepository.findUserByCrew(crew);

        //then
        Assertions.assertThat(findUserList.size()).isEqualTo(1);
        Assertions.assertThat(findUserList.get(0)).isEqualTo(user);
    }


    @DisplayName("유저가 작성한 크루가입 답변 삭제하기")
    @Test
    void deleteByUserAndCrewTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);

        User user = testUser(dongArea);
        Crew crew = testCrew(dongArea);
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 1));
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 2));
        recruitAnswerRepository.save(new RecruitAnswer(user, crew, "answer", 3));

        //when
        List<RecruitAnswer> findBeforeList = recruitAnswerRepository.findAllByUserAndCrew(user, crew);
        Assertions.assertThat(findBeforeList.size()).isEqualTo(3);

        recruitAnswerRepository.deleteByUserAndCrew(user, crew);
        List<RecruitAnswer> findAfterList = recruitAnswerRepository.findAllByUserAndCrew(user, crew);

        //then
        Assertions.assertThat(findAfterList.size()).isEqualTo(0);
    }

    @DisplayName("크루에 가입신청 한 유저 리스트 반환")
    @Test
    void nameTest() throws Exception {
        //given

        //when

        //then

    }


}