package com.project.runningcrew.board.repository;

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
import com.project.runningcrew.TestEntityFactory;
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

    }


    @DisplayName("ReviewBoard findById 테스트")
    @Test
    void findByIdTest() throws Exception {

    }


    @DisplayName("ReviewBoard delete 테스트")
    @Test
    void deleteTest() throws Exception {

    }


    @DisplayName("특정 crew 의 ReviewBoard 페이징 출력 테스트")
    @Test
    void findReviewBoardByCrew() throws Exception {

    }


}