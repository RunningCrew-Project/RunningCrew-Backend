package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.comment.RunningNoticeComment;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.*;
import com.project.runningcrew.repository.boards.BoardRepository;
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
class RunningNoticeCommentRepositoryTest {


    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;

    @Autowired BoardRepository boardRepository;
    @Autowired RunningNoticeCommentRepository runningNoticeCommentRepository;
    @Autowired RunningNoticeRepository runningNoticeRepository;
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




    @DisplayName("특정 RunningNotice 의 모든 Comment 출력 테스트")
    @Test
    void findAllByRunningNoticeTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);

        User user1 = testUser(dongArea, 1);
        User user2 = testUser(dongArea, 2);

        Crew crew1 = testCrew(dongArea, 1);
        Crew crew2 = testCrew(dongArea, 2);

        Member memberA = testMember(user1, crew1); // user(1), crew(1)
        Member memberB = testMember(user2, crew2); // user(2), crew(2)
        RunningNotice testRunningNoticeA = testRunningNotice(memberA);
        RunningNotice testRunningNoticeB = testRunningNotice(memberB);

        RunningNoticeComment comment_1 =
                runningNoticeCommentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNoticeA));
        RunningNoticeComment comment_2 =
                runningNoticeCommentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNoticeA));
        RunningNoticeComment comment_3 =
                runningNoticeCommentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNoticeB));

        //when
        List<RunningNoticeComment> findRunningNoticeCommentListA =
                runningNoticeCommentRepository.findAllByRunningNotice(testRunningNoticeA);
        List<RunningNoticeComment> findRunningNoticeCommentListB =
                runningNoticeCommentRepository.findAllByRunningNotice(testRunningNoticeB);

        //then
        Assertions.assertThat(findRunningNoticeCommentListA.size()).isEqualTo(2);
        Assertions.assertThat(findRunningNoticeCommentListB.size()).isEqualTo(1);

    }


    @DisplayName("RunningNotice id 리스트를 받아 commentCount 리스트를 반환하는 테스트")
    @Test
    void countByRunningNoticeIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);

        User user1 = testUser(dongArea, 1);
        User user2 = testUser(dongArea, 2);

        Crew crew1 = testCrew(dongArea, 1);
        Crew crew2 = testCrew(dongArea, 2);

        Member memberA = testMember(user1, crew1); // user(1), crew(1)
        Member memberB = testMember(user2, crew2); // user(2), crew(2)
        RunningNotice testRunningNoticeA = testRunningNotice(memberA);
        RunningNotice testRunningNoticeB = testRunningNotice(memberB);

        RunningNoticeComment comment_1 =
                runningNoticeCommentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNoticeA));
        RunningNoticeComment comment_2 =
                runningNoticeCommentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNoticeA));
        RunningNoticeComment comment_3 =
                runningNoticeCommentRepository.save(new RunningNoticeComment(memberA, "detail", testRunningNoticeB));

        List<Long> runningNoticeIdList = new ArrayList<>();
        runningNoticeIdList.add(testRunningNoticeA.getId());
        runningNoticeIdList.add(testRunningNoticeB.getId());

        //when
        List<Integer> commentCountList = runningNoticeCommentRepository.countByRunningNoticeId(runningNoticeIdList);

        //then
        Assertions.assertThat(commentCountList.size()).isEqualTo(2);
        Assertions.assertThat(commentCountList.get(0)).isEqualTo(2);
        Assertions.assertThat(commentCountList.get(1)).isEqualTo(1);
    }



















}