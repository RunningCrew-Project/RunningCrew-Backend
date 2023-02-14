package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.comment.BoardComment;
import com.project.runningcrew.entity.comment.Comment;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.comment.RunningNoticeComment;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.RunningNoticeRepository;
import com.project.runningcrew.repository.UserRepository;
import com.project.runningcrew.repository.boards.BoardRepository;
import com.project.runningcrew.repository.comment.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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


    public User testUser(int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!")
                .name("name")
                .nickname("nickname" + num)
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
                .name("name" + num)
                .location("location")
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember(int num) {
        Member member = new Member(testUser(num), testCrew(num), MemberRole.ROLE_NORMAL);
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
                .status(RunningStatus.WAIT)
                .build();
        return runningNoticeRepository.save(runningNotice);
    }




    @DisplayName("특정 MemberId 를 가진 Comment 출력 테스트")
    @Test
    void findAllByMemberIdTest() throws Exception {
        //given
        Member memberA = testMember(1); // user(1), crew(1)
        Member memberB = testMember(2); // user(2), crew(2)

        Member testCreateMember = testMember(3); // user(3), crew(3)
        FreeBoard testFreeBoard = boardRepository.save(new FreeBoard(testCreateMember, "title", "content"));
        RunningNotice testRunningNotice = runningNoticeRepository.save(testRunningNotice(testCreateMember));


        BoardComment comment_1 = commentRepository.save(new BoardComment(memberA, "detail", testFreeBoard));
        RunningNoticeComment comment_2 = commentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNotice));
            // memberA

        RunningNoticeComment comment_3 = commentRepository.save(new RunningNoticeComment(memberB, "detail", testRunningNotice));
            // memberB

        //when
        List<Comment> findCommentListA = commentRepository.findAllByMember(memberA);
        List<Comment> findCommentListB = commentRepository.findAllByMember(memberB);

        //then
        Assertions.assertThat(findCommentListA.size()).isEqualTo(2);
        Assertions.assertThat(findCommentListB.size()).isEqualTo(1);
    }


}