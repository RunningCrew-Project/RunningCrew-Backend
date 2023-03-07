package com.project.runningcrew.repository.boards;

import com.project.runningcrew.board.repository.ReviewBoardRepository;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.repository.TestEntityFactory;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.runningrecord.repository.PersonalRunningRecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class ReviewBoardRepositoryTest {

    /**
     * 본 테스트의 RunningRecord 의 구현체로 PersonalRunningRecord 를 사용함.
     */
    @Autowired
    UserRepository userRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PersonalRunningRecordRepository personalRunningRecordRepository;
    @Autowired
    ReviewBoardRepository reviewBoardRepository;
    @Autowired
    TestEntityFactory testEntityFactory;


    public User testUser(DongArea dongArea, int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!")
                .name("name")
                .nickname("nickname" + num)
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

    public Crew testCrew(DongArea dongArea, int num) {
        Crew crew = Crew.builder()
                .name("name" + num)
                .dongArea(dongArea)
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember(User user, Crew crew) {
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }

    public PersonalRunningRecord testPersonalRunningRecord(User user, int num) {
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .title("personal")
                .startDateTime(LocalDateTime.now())
                .location("location")
                .runningDistance(100)
                .runningTime(100)
                .runningFace(100)
                .calories(100)
                .running_detail("running_detail")
                .user(user)
                .build();
        return personalRunningRecord;
    }


    @DisplayName("ReviewBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String detail = "detail";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);

        //when
        ReviewBoard reviewBoard = new ReviewBoard(member, title, detail, testPersonalRunningRecord(user, num));
        ReviewBoard savedReviewBoard = reviewBoardRepository.save(reviewBoard);

        //then
        Assertions.assertThat(savedReviewBoard).isEqualTo(reviewBoard);
    }


    @DisplayName("ReviewBoard findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String detail = "detail";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);

        ReviewBoard savedReviewBoard = reviewBoardRepository.save(new ReviewBoard(member, title, detail, testPersonalRunningRecord(user, num)));

        //when
        Optional<ReviewBoard> findReviewBoardOpt = reviewBoardRepository.findById(savedReviewBoard.getId());

        //then
        Assertions.assertThat(findReviewBoardOpt).isNotEmpty();
        Assertions.assertThat(findReviewBoardOpt).hasValue(savedReviewBoard);
    }


    @DisplayName("ReviewBoard delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String detail = "detail";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        ReviewBoard savedReviewBoard = reviewBoardRepository.save(new ReviewBoard(member, title, detail, testPersonalRunningRecord(user, num)));

        //when
        reviewBoardRepository.delete(savedReviewBoard);
        Optional<ReviewBoard> findReviewBoardOpt = reviewBoardRepository.findById(savedReviewBoard.getId());

        //then
        Assertions.assertThat(findReviewBoardOpt).isEmpty();
    }


    @DisplayName("특정 crew 의 ReviewBoard 페이징 출력 테스트")
    @Test
    void findReviewBoardByCrew() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew); // user(1), crew(1)

        PersonalRunningRecord personalRunningRecord = personalRunningRecordRepository.save(
                PersonalRunningRecord.builder()
                        .title("personal")
                        .startDateTime(LocalDateTime.now())
                        .location("location")
                        .runningDistance(100)
                        .runningTime(100)
                        .runningFace(100)
                        .calories(100)
                        .running_detail("running_detail")
                        .user(user)
                        .build()
        );

        for (int i = 0; i < 100; i++) {
            reviewBoardRepository.save(
                    new ReviewBoard(member, "title" + i, "detail" + i, personalRunningRecord)
                    // ReviewBoard 100개 save
            );
        }
        PageRequest pageRequest = PageRequest.of(0, 15); // Page size = 15

        //when
        Slice<ReviewBoard> slice = reviewBoardRepository.findReviewBoardByCrew(member.getCrew(), pageRequest);
        List<ReviewBoard> content = slice.getContent();

        //then
        Assertions.assertThat(content.size()).isEqualTo(15);
        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        Assertions.assertThat(slice.getNumberOfElements()).isEqualTo(15);
        Assertions.assertThat(slice.isFirst()).isTrue();
        Assertions.assertThat(slice.hasNext()).isTrue();
    }


}