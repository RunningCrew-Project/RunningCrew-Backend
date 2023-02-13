package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    TestEntityFactory testEntityFactory;

    @DisplayName("RunningMember save 테스트")
    @Test
    public void saveTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);

        ///when
        RunningMember savedRunningMember = runningMemberRepository.save(runningMember);

        //then
        assertThat(savedRunningMember).isEqualTo(runningMember);
    }

    @DisplayName("RunningMember findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        ///when
        Optional<RunningMember> optRunningMember = runningMemberRepository.findById(runningMember.getId());

        //then
        assertThat(optRunningMember).isNotEmpty();
        assertThat(optRunningMember).hasValue(runningMember);
    }

    @DisplayName("RunningMember delete 테스트")
    @Test
    public void deleteTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        ///when
        runningMemberRepository.delete(runningMember);

        //then
        Optional<RunningMember> optRunningMember = runningMemberRepository.findById(runningMember.getId());
        assertThat(optRunningMember).isEmpty();
    }

    @DisplayName("RunningMember findAllByMember 테스트")
    @Test
    void findAllByMemberTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            User tempUser = testEntityFactory.getUser(i);
            Member tempMember = testEntityFactory.getMember(tempUser, crew);
            RunningNotice tempRunningNotice = testEntityFactory.getRunningNotice(tempMember, i);
            RunningMember tempRunningMember = new RunningMember(tempRunningNotice, member);
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

    @DisplayName("RunningMember countAllByRunningNotice 테스트")
    @Test
    void countAllByRunningNoticeTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            User tempUser = testEntityFactory.getUser(i);
            Member tempMember = testEntityFactory.getMember(tempUser, crew);
            RunningMember tempRunningMember = new RunningMember(runningNotice, tempMember);
            runningMemberRepository.save(tempRunningMember);
        }

        //when
        Long count = runningMemberRepository.countAllByRunningNotice(runningNotice);

        //then
        assertThat(count).isSameAs(10L);
    }

    @DisplayName("RunningMember findAllByRunningNotice 테스트")
    @Test
    void findAllByRunningNoticeTest() {
        //given
        User user = testEntityFactory.getUser(0);
        Crew crew = testEntityFactory.getCrew(0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            User tempUser = testEntityFactory.getUser(i);
            Member tempMember = testEntityFactory.getMember(tempUser, crew);
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