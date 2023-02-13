package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.UserRepository;
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
class NoticeBoardRepositoryTest {


    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired NoticeBoardRepository noticeBoardRepository;

    public User testUser(int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!")
                .name("name")
                .nickname("nickname"+ num)
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

    public Crew testCrew(int num) {
        Crew crew = Crew.builder()
                .name("name"+ num)
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember(int num) {
        Member member = new Member(testUser(num), testCrew(num), MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }



    @DisplayName("NoticeBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        Member member = testMember(1);
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
        Member member = testMember(1);
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
        Member member = testMember(1);
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


}