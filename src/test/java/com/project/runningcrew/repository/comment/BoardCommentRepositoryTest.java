package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.boards.ReviewBoard;
import com.project.runningcrew.entity.comment.BoardComment;
import com.project.runningcrew.entity.comment.Comment;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.runningrecords.CrewRunningRecord;
import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.RunningNoticeRepository;
import com.project.runningcrew.repository.UserRepository;
import com.project.runningcrew.repository.boards.BoardRepository;
import com.project.runningcrew.repository.runningrecords.CrewRunningRecordRepository;
import com.project.runningcrew.repository.runningrecords.PersonalRunningRecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardCommentRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;

    @Autowired BoardRepository boardRepository;
    @Autowired BoardCommentRepository boardCommentRepository;
    @Autowired PersonalRunningRecordRepository personalRunningRecordRepository;


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

    public PersonalRunningRecord testPersonalRunningRecord(User user) {
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        return personalRunningRecordRepository.save(personalRunningRecord);
    }




    @DisplayName("특정 Board 의 모든 Comment 출력 테스트")
    @Test
    void findAllByBoardTest() throws Exception {
        //given
        Member createMember = testMember(1); // user(1), crew(1)
        FreeBoard testFreeBoard =
                boardRepository.save(new FreeBoard(createMember, "title", "content"));
        ReviewBoard testReviewBoard =
                boardRepository.save(new ReviewBoard(createMember, "title", "content", testPersonalRunningRecord(createMember.getUser())));

        BoardComment comment_1 = boardCommentRepository.save(new BoardComment(createMember, "detail", testFreeBoard));
        BoardComment comment_2 = boardCommentRepository.save(new BoardComment(createMember, "detail", testFreeBoard));
            // testFreeBoard
        BoardComment comment_3 = boardCommentRepository.save(new BoardComment(createMember, "detail", testReviewBoard));
            // testReviewBoard

        //when
        List<BoardComment> findBoardCommentListA = boardCommentRepository.findAllByBoard(testFreeBoard);
        List<BoardComment> findBoardCommentListB = boardCommentRepository.findAllByBoard(testReviewBoard);

        //then
        Assertions.assertThat(findBoardCommentListA.size()).isEqualTo(2);
        Assertions.assertThat(findBoardCommentListB.size()).isEqualTo(1);
    }


}