package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RunningMemberRepositoryTest {

    @Autowired
    RunningMemberRepository runningMemberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RunningNoticeRepository runningNoticeRepository;

    public User testUser(int num) {
        User user = User.builder().email("email" + num + "@naver.com")
                .name("name" + num)
                .nickname("nickname" + num)
                .imgUrl("imgUrl" + num)
                .login_type(LoginType.EMAIL)
                .phoneNumber("010-0000-0000")
                .password("123a!")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .height(170)
                .weight(70)
                .build();
        return userRepository.save(user);
    }

    public Crew testCrew(int num) {
        Crew crew = Crew.builder().name("crew" + num)
                .location("location" + num)
                .introduction("introduction" + num)
                .crewImgUrl("crewImageUrl" + num)
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember(User user, Crew crew) {
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }

    public RunningNotice testRunningNotice(Member member) {
        RunningNotice runningNotice = RunningNotice.builder().title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 02, 11, 15, 0))
                .runningPersonnel(4)
                .status(RunningStatus.WAIT)
                .build();
        return runningNoticeRepository.save(runningNotice);
    }

    @Test
    public void saveTest() {
        //given
        Member member = testMember(testUser(0), testCrew(0));
        RunningNotice runningNotice = testRunningNotice(member);
        RunningMember runningMember = new RunningMember(runningNotice, member);

        ///when
        RunningMember savedRunningMember = runningMemberRepository.save(runningMember);

        //then
        assertThat(savedRunningMember).isEqualTo(runningMember);
    }

    @Test
    public void findByIdTest() {
        //given
        Member member = testMember(testUser(0), testCrew(0));
        RunningNotice runningNotice = testRunningNotice(member);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        ///when
        Optional<RunningMember> optRunningMember = runningMemberRepository.findById(runningMember.getId());

        //then
        assertThat(optRunningMember).isNotEmpty();
        assertThat(optRunningMember).hasValue(runningMember);
    }

    @Test
    public void deleteTest() {
        //given
        Member member = testMember(testUser(0), testCrew(0));
        RunningNotice runningNotice = testRunningNotice(member);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        ///when
        runningMemberRepository.delete(runningMember);

        //then
        Optional<RunningMember> optRunningMember = runningMemberRepository.findById(runningMember.getId());
        assertThat(optRunningMember).isEmpty();
    }

    @Test
    void findAllByMemberTest() {
        //given
        User user = testUser(0);
        Crew crew = testCrew(0);
        Member member = testMember(user, crew);
        RunningNotice runningNotice = testRunningNotice(member);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            Member tempMember = testMember(testUser(i), crew);
            RunningNotice tempRunningNotice = testRunningNotice(tempMember);
            RunningMember tempRunningMember = new RunningMember(runningNotice, member);
            runningMemberRepository.save(tempRunningMember);
        }

        //when
        List<RunningMember> runningMembers = runningMemberRepository.findAllByMember(member);

        //then
        assertThat(runningMembers.size()).isSameAs(10);
        for (RunningMember tempRunningMember : runningMembers) {
            assertThat(tempRunningMember.getMember()).isEqualTo(member);
        }
    }

    @Test
    void countAllByRunningNoticeTest() {
        //given
        Crew crew = testCrew(0);
        Member member = testMember(testUser(0), crew);
        RunningNotice runningNotice = testRunningNotice(member);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            Member tempMember = testMember(testUser(i), crew);
            RunningMember tempRunningMember = new RunningMember(runningNotice, tempMember);
            runningMemberRepository.save(tempRunningMember);
        }

        //when
        Long count = runningMemberRepository.countAllByRunningNotice(runningNotice);

        //then
        assertThat(count).isSameAs(10L);
    }

    @Test
    void findAllByRunningNoticeTest() {
        //given
        Crew crew = testCrew(0);
        Member member = testMember(testUser(0), crew);
        RunningNotice runningNotice = testRunningNotice(member);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            Member tempMember = testMember(testUser(i), crew);
            RunningMember tempRunningMember = new RunningMember(runningNotice, tempMember);
            runningMemberRepository.save(tempRunningMember);
        }

        //when
        List<RunningMember> runningMembers = runningMemberRepository.findAllByRunningNotice(runningNotice);

        //then
        assertThat(runningMembers.size()).isSameAs(10);
        for (RunningMember tempRunningMember : runningMembers) {
            assertThat(tempRunningMember.getRunningNotice()).isEqualTo(runningNotice);
        }
    }

}