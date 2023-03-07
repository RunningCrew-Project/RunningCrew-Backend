package com.project.runningcrew.repository.comment;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.repository.CommentRepository;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.entity.RunningStatus;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.runningnotice.repository.RunningNoticeRepository;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.repository.*;
import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.user.repository.UserRepository;
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
class CommentRepositoryTest {

    /**
     * 테스트 용도 Board 는 FreeBoard 객체를 구현함.
     */

    @Autowired
    UserRepository userRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    RunningNoticeRepository runningNoticeRepository;
    @Autowired TestEntityFactory testEntityFactory;


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

    public RunningNotice testRunningNotice(Member member) {
        RunningNotice runningNotice = RunningNotice.builder()
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(LocalDateTime.now())
                .runningPersonnel(100)
                .status(RunningStatus.READY)
                .build();
        return runningNoticeRepository.save(runningNotice);
    }




    @DisplayName("특정 MemberId 를 가진 Comment 출력 테스트 - 페이징 적용")
    @Test
    void findAllByMemberIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);

        User user1 = testUser(dongArea, 1);
        User user2 = testUser(dongArea, 2);
        User user3 = testUser(dongArea, 3);

        Crew crew1 = testCrew(dongArea, 1);
        Crew crew2 = testCrew(dongArea, 2);
        Crew crew3 = testCrew(dongArea, 3);

        Member memberA = testMember(user1, crew1); // user(1), crew(1)
        Member memberB = testMember(user2, crew2); // user(2), crew(2)
        Member testCreateMember = testMember(user3, crew3); // user(3), crew(3)

        FreeBoard testFreeBoard = boardRepository.save(new FreeBoard(testCreateMember, "title", "content"));
        RunningNotice testRunningNotice = runningNoticeRepository.save(testRunningNotice(testCreateMember));


        BoardComment comment_1 = commentRepository.save(new BoardComment(memberA, "detail", testFreeBoard));
        RunningNoticeComment comment_2 = commentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNotice));
        RunningNoticeComment comment_3 = commentRepository.save(new RunningNoticeComment(memberB, "detail", testRunningNotice));

        //when
        PageRequest pageRequest = PageRequest.of(0, 5);
        Slice<Comment> findCommentListA = commentRepository.findAllByMember(memberA, pageRequest);
        Slice<Comment> findCommentListB = commentRepository.findAllByMember(memberB, pageRequest);

        List<Comment> contentA = findCommentListA.getContent();
        List<Comment> contentB = findCommentListB.getContent();

        //then
        Assertions.assertThat(contentA.size()).isEqualTo(2);
        Assertions.assertThat(contentB.size()).isEqualTo(1);

    }


}