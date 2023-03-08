package com.project.runningcrew.board.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
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

@SpringBootTest
@Transactional
class BoardRepositoryTest {


    @Autowired
    FreeBoardRepository freeBoardRepository;
    @Autowired
    ReviewBoardRepository reviewBoardRepository;
    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PersonalRunningRecordRepository personalRunningRecordRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired TestEntityFactory testEntityFactory;


    public User testUser(DongArea dongArea, int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!")
                .name("name")
                .nickname("nickname"+ num)
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
                .name("name"+ num)
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
                .startDateTime(LocalDateTime.now())
                .runningDistance(100)
                .runningTime(100)
                .runningFace(100)
                .calories(100)
                .running_detail("running_detail")
                .user(user)
                .build();
        return personalRunningRecordRepository.save(personalRunningRecord);
    }


    @DisplayName("특정 Member 가 작성한 모든 게시물 출력 테스트")
    @Test
    void findAllByMemberTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);

        User user = testUser(dongArea, 1);
        User user2 = testUser(dongArea, 2);

        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew); // user(1), crew(1)
        Member member2 = testMember(user2, crew);

        PersonalRunningRecord personalRunningRecord = personalRunningRecordRepository.save(
                PersonalRunningRecord.builder()
                        .startDateTime(LocalDateTime.now())
                        .runningDistance(100)
                        .runningTime(100)
                        .runningFace(100)
                        .calories(100)
                        .title("title")
                        .running_detail("running_detail")
                        .user(member.getUser())
                        .build()
        );

        FreeBoard freeBoard = freeBoardRepository.save(
                new FreeBoard(member, "title", "content")
        );

        FreeBoard freeBoard2 = freeBoardRepository.save(
                new FreeBoard(member, "title", "content")
        );

        ReviewBoard reviewBoard = reviewBoardRepository.save(
                new ReviewBoard(member, "title", "content", personalRunningRecord)
        );

        PageRequest pageRequest = PageRequest.of(0, 5);
        Slice<Board> findSlice = boardRepository.findByMember(member, pageRequest);
        Slice<Board> findSlice2 = boardRepository.findByMember(member2, pageRequest);

        List<Board> content = findSlice.getContent();
        List<Board> content2 = findSlice2.getContent();

        //then
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(content2.size()).isEqualTo(0);

        Assertions.assertThat(findSlice.isFirst()).isTrue();
        Assertions.assertThat(findSlice2.isFirst()).isTrue();
        Assertions.assertThat(findSlice.getNumberOfElements()).isEqualTo(3);
        Assertions.assertThat(findSlice2.getNumberOfElements()).isEqualTo(0);
    }


    @DisplayName("특정 keyword 를 포함하는 모든 게시물 출력 테스트")
    @Test
    void findListAllByKeywordAndCrewTest() throws Exception {
        //given
        String keyword = "key";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);

        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);


        PersonalRunningRecord personalRunningRecord = personalRunningRecordRepository.save(
                PersonalRunningRecord.builder()
                        .startDateTime(LocalDateTime.now())
                        .runningDistance(100)
                        .runningTime(100)
                        .runningFace(100)
                        .calories(100)
                        .title("title")
                        .running_detail("running_detail")
                        .user(member.getUser())
                        .build()
        );

        FreeBoard freeBoard = freeBoardRepository.save(
                new FreeBoard(member, "title", "cont_key_ent")
        ); // FreeBoard, keyword content 포함

        FreeBoard freeBoard2 = freeBoardRepository.save(
                new FreeBoard(member, "title", "content")
        ); // FreeBoard, keyword 미포함

        ReviewBoard reviewBoard = reviewBoardRepository.save(
                new ReviewBoard(member, "tit_key_le", "content", personalRunningRecord)
        ); // ReviewBoard, keyword title 포함

        //when
        List<Board> findBoardList = boardRepository.findListAllByCrewAndKeyWord(keyword, crew);
        //then
        Assertions.assertThat(findBoardList.size()).isEqualTo(2);
    }





    @DisplayName("특정 keyword 를 포함하는 모든 게시물 출력 테스트 paging 적용")
    @Test
    void findSliceAllByKeywordAndCrewTest() throws Exception {
        //given
        String keyword = "key";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew); // user(1), crew(1)

        PersonalRunningRecord personalRunningRecord = personalRunningRecordRepository.save(
                PersonalRunningRecord.builder()
                        .startDateTime(LocalDateTime.now())
                        .runningDistance(100)
                        .runningTime(100)
                        .runningFace(100)
                        .calories(100)
                        .title("title")
                        .running_detail("running_detail")
                        .user(member.getUser())
                        .build()
        );

        FreeBoard freeBoard = freeBoardRepository.save(
                new FreeBoard(member, "title", "cont_key_ent")
        ); // FreeBoard, keyword content 포함

        FreeBoard freeBoard2 = freeBoardRepository.save(
                new FreeBoard(member, "title", "content")
        ); // FreeBoard, keyword 미포함

        ReviewBoard reviewBoard = reviewBoardRepository.save(
                new ReviewBoard(member, "tit_key_le", "content", personalRunningRecord)
        ); // ReviewBoard, keyword title 포함

        //when
        Slice<Board> slice = boardRepository.findSliceAllByCrewAndKeyWord(keyword,crew);
        List<Board> content = slice.getContent();

        //then
        Assertions.assertThat(content.size()).isEqualTo(2);
        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        Assertions.assertThat(slice.getNumberOfElements()).isEqualTo(2);
        Assertions.assertThat(slice.isFirst()).isTrue();
        Assertions.assertThat(slice.hasNext()).isFalse();

    }



}