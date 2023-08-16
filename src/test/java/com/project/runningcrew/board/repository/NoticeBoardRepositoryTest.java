package com.project.runningcrew.board.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class NoticeBoardRepositoryTest {


    @Mock
    UserRepository userRepository;

    @Mock
    CrewRepository crewRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    NoticeBoardRepository noticeBoardRepository;

    @Mock
    TestEntityFactory testEntityFactory;


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



    @DisplayName("NoticeBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        String title = "title";
        String content = "content";
        NoticeBoard noticeBoard = new NoticeBoard(member, title, content);

        //when
        NoticeBoard savedNoticeBoard = noticeBoardRepository.save(noticeBoard);

        //then
        Assertions.assertThat(savedNoticeBoard).isEqualTo(noticeBoard);
    }



    @DisplayName("NoticeBoard findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        String title = "title";
        String content = "content";
        NoticeBoard noticeBoard = new NoticeBoard(member, title, content);
        NoticeBoard savedNoticeBoard = noticeBoardRepository.save(noticeBoard);

        //when
        Optional<NoticeBoard> findNoticeBoardOpt = noticeBoardRepository.findById(savedNoticeBoard.getId());

        //then
        Assertions.assertThat(findNoticeBoardOpt).isNotEmpty();
        Assertions.assertThat(findNoticeBoardOpt).hasValue(savedNoticeBoard);
    }



    @DisplayName("NoticeBoard delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        String title = "title";
        String content = "content";
        NoticeBoard noticeBoard = new NoticeBoard(member, title, content);
        NoticeBoard savedNoticeBoard = noticeBoardRepository.save(noticeBoard);

        //when
        noticeBoardRepository.delete(savedNoticeBoard);
        Optional<NoticeBoard> findNoticeBoardOpt = noticeBoardRepository.findById(savedNoticeBoard.getId());

        //then
        Assertions.assertThat(findNoticeBoardOpt).isEmpty();
    }



    @DisplayName("각 Crew 의 공지게시판 paging 출력 테스트")
    @Test
    void nameTest() throws Exception {

    }

}