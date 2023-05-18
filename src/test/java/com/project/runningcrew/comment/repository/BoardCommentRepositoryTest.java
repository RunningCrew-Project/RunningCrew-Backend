package com.project.runningcrew.comment.repository;

import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.runningrecord.repository.PersonalRunningRecordRepository;
import com.project.runningcrew.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class BoardCommentRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired BoardRepository boardRepository;
    @Autowired
    BoardCommentRepository boardCommentRepository;
    @Autowired PersonalRunningRecordRepository personalRunningRecordRepository;
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
        return memberRepository.save(member); // member(num) -> user(num), crew(num) 생성
    }

    public PersonalRunningRecord testPersonalRunningRecord(User user) {
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .title("title")
                .running_detail("")
                .user(user)
                .build();
        return personalRunningRecordRepository.save(personalRunningRecord);
    }




    @DisplayName("특정 Board 의 모든 Comment 출력 테스트")
    @Test
    void findAllByBoardTest() throws Exception {

    }


    @DisplayName("Board id 리스트를 받아 commentCount 리스트를 반환하는 테스트")
    @Test
    void countByBoardIdTest() throws Exception {

    }


}