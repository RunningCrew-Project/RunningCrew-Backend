package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.boards.ReviewBoard;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.UserRepository;
import com.project.runningcrew.repository.runningrecords.CrewRunningRecordRepository;
import com.project.runningcrew.repository.runningrecords.PersonalRunningRecordRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewBoardRepositoryTest {

    /**
     * 본 테스트의 RunningRecord 의 구현체로 PersonalRunningRecord 를 사용함.
     */
    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PersonalRunningRecordRepository personalRunningRecordRepository;
    @Autowired ReviewBoardRepository reviewBoardRepository;


    public User testUser(int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!" + num)
                .name("name"+ num)
                .nickname("nickname"+ num)
                .imgUrl("imgUrl"+ num)
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber"+ num)
                .location("location"+ num)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        return userRepository.save(user);
    }

    public Crew testCrew(int num) {
        Crew crew = Crew.builder()
                .name("name"+ num)
                .location("location"+ num)
                .introduction("introduction"+ num)
                .crewImgUrl("crewImgUrl"+ num)
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember(int num) {
        Member member = new Member(testUser(num), testCrew(num), MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }

    public PersonalRunningRecord testPersonalRunningRecord(int num) {
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                        .startDateTime(LocalDateTime.now())
                        .runningDistance(100)
                        .runningTime(100)
                        .runningFace(100)
                        .calories(100)
                        .running_detail("running_detail")
                        .user(testUser(num))
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

        //when
        ReviewBoard reviewBoard = new ReviewBoard(testMember(num), title, detail, testPersonalRunningRecord(num));
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
        ReviewBoard savedReviewBoard = reviewBoardRepository.save(new ReviewBoard(testMember(num), title, detail, testPersonalRunningRecord(num)));

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
        ReviewBoard savedReviewBoard = reviewBoardRepository.save(new ReviewBoard(testMember(num), title, detail, testPersonalRunningRecord(num)));

        //when
        reviewBoardRepository.delete(savedReviewBoard);
        Optional<ReviewBoard> findReviewBoardOpt = reviewBoardRepository.findById(savedReviewBoard.getId());

        //then
        Assertions.assertThat(findReviewBoardOpt).isEmpty();
    }




    @DisplayName("ReviewBoard 페이징 테스트")
    @Test
    void findReviewBoardAllTest() throws Exception {
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

        Member member = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));

        PersonalRunningRecord personalRunningRecord = personalRunningRecordRepository.save(
                PersonalRunningRecord.builder()
                        .startDateTime(LocalDateTime.now())
                        .runningDistance(100)
                        .runningTime(100)
                        .runningFace(100)
                        .calories(100)
                        .running_detail("running_detail")
                        .user(user)
                        .build()
        );

        String title = "title";
        String detail = "detail";
        for (int i = 0; i < 100; i++) {
            reviewBoardRepository.save(
                    new ReviewBoard(member, title + i, detail + i, personalRunningRecord)
                    // ReviewBoard 100개 save
            );
        }
        PageRequest pageRequest = PageRequest.of(0, 15); // Page size = 15

        //when
        Slice<ReviewBoard> slice = reviewBoardRepository.findReviewBoardAll(pageRequest);
        List<ReviewBoard> content = slice.getContent();

        //then
        Assertions.assertThat(content.size()).isEqualTo(15);
        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        Assertions.assertThat(slice.getNumberOfElements()).isEqualTo(15);
        Assertions.assertThat(slice.isFirst()).isTrue();
        Assertions.assertThat(slice.hasNext()).isTrue();
    }


}