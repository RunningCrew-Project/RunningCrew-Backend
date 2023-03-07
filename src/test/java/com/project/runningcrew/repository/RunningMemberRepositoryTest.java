package com.project.runningcrew.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.runningmember.entity.RunningMember;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningmember.repository.RunningMemberRepository;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            User tempUser = testEntityFactory.getUser(dongArea, i);
            Member tempMember = testEntityFactory.getMember(tempUser, crew);
            RunningNotice tempRunningNotice = testEntityFactory.getRegularRunningNotice(tempMember, i);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            User tempUser = testEntityFactory.getUser(dongArea, i);
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
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            User tempUser = testEntityFactory.getUser(dongArea, i);
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

    @DisplayName("Member 와 RunningNotice 에 해당하는 RunningMember 반환 성공 테스트")
    @Test
    public void findByMemberAndRunningNoticeTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        ///when
        Optional<RunningMember> optRunningMember = runningMemberRepository.findByMemberAndRunningNotice(member, runningNotice);

        //then
        assertThat(optRunningMember).isNotEmpty();
        assertThat(optRunningMember).hasValue(runningMember);
    }

    @DisplayName("Member 와 RunningNotice 에 해당하는 RunningMember 반환 실패 테스트")
    @Test
    public void findByMemberAndRunningNoticeTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);

        ///when
        Optional<RunningMember> optRunningMember = runningMemberRepository.findByMemberAndRunningNotice(member, runningNotice);

        //then
        assertThat(optRunningMember).isEmpty();
    }

    @DisplayName("Member 의 RunningNotice 참여 테스트")
    @Test
    public void existsByMemberAndRunningNoticeTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        ///when
        boolean result = runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("Member 의 RunningNotice 참여 안함 테스트")
    @Test
    public void existsByMemberAndRunningNoticeTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);

        ///when
        boolean result = runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("RunningNotice 에 포함된 모든 RunningMember 삭제 테스트")
    @Test
    public void deleteAllByRunningNoticeTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
        RunningMember runningMember = new RunningMember(runningNotice, member);
        runningMemberRepository.save(runningMember);

        for (int i = 1; i < 10; i++) {
            User tempUser = testEntityFactory.getUser(dongArea, i);
            Member tempMember = testEntityFactory.getMember(tempUser, crew);
            RunningMember tempRunningMember = new RunningMember(runningNotice, tempMember);
            runningMemberRepository.save(tempRunningMember);
        }

        ///when
        runningMemberRepository.deleteAllByRunningNotice(runningNotice);

        //then
        List<RunningMember> runningMembers = runningMemberRepository.findAllByRunningNotice(runningNotice);
        assertThat(runningMembers).isEmpty();
    }

}