package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("Member save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);

        ///when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember).isEqualTo(member);

    }

    @DisplayName("Member findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        memberRepository.save(member);

        ///when
        Optional<Member> optMember = memberRepository.findById(member.getId());

        //then
        assertThat(optMember).isNotEmpty();
        assertThat(optMember).hasValue(member);
    }

    @DisplayName("Member delete 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        Member savedMember = memberRepository.save(member);

        ///when
        memberRepository.delete(savedMember);

        //then
        Optional<Member> optMember = memberRepository.findById(savedMember.getId());
        assertThat(optMember).isEmpty();
    }

    @DisplayName("Member findAllByUser 테스트")
    @Test
    void findAllByUserTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);

        for (int i = 0; i < 10; i++) {
            Crew crew = testEntityFactory.getCrew(dongArea, i);
            Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        ///when
        List<Member> members = memberRepository.findAllByUser(user);

        //then
        assertThat(members.size()).isSameAs(10);
        for (Member member : members) {
            assertThat(member.getUser()).isEqualTo(user);
        }
    }

    @DisplayName("Member countAllByCrew 테스트")
    @Test
    void countAllByCrewTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);

        for (int i = 0; i < 10; i++) {
            User user = testEntityFactory.getUser(dongArea, i);
            Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        ///when
        Long count = memberRepository.countAllByCrew(crew);

        //then
        assertThat(count).isSameAs(10L);
    }

    @DisplayName("Member findAllByCrew 테스트")
    @Test
    void findAllByCrewTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);

        for (int i = 0; i < 10; i++) {
            User user = testEntityFactory.getUser(dongArea, i);
            Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        ///when
        List<Member> members = memberRepository.findAllByCrew(crew);

        //then
        assertThat(members.size()).isSameAs(10);
        for (Member member : members) {
            assertThat(member.getCrew()).isEqualTo(crew);
        }
    }

    @DisplayName("Member findAllByCrewAndRole 테스트")
    @Test
    void findAllByCrewAndRoleTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);

        for (int i = 0; i < 10; i++) {
            User user = testEntityFactory.getUser(dongArea, i);

            Member member;
            if (i == 0) {
                member = new Member(user, crew, MemberRole.ROLE_LEADER);
            } else if (i >= 1 && i < 5) {
                member = new Member(user, crew, MemberRole.ROLE_MANAGER);
            } else {
                member = new Member(user, crew, MemberRole.ROLE_NORMAL);
            }
            memberRepository.save(member);
        }

        ///when
        List<Member> leaderMembers = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_LEADER);
        List<Member> managerMembers = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_MANAGER);
        List<Member> normalMembers = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_NORMAL);

        //then
        assertThat(leaderMembers.size()).isSameAs(1);
        assertThat(managerMembers.size()).isSameAs(4);
        assertThat(normalMembers.size()).isSameAs(5);

        for (Member leaderMember : leaderMembers) {
            assertThat(leaderMember.getRole()).isSameAs(MemberRole.ROLE_LEADER);
        }
        for (Member managerMember : managerMembers) {
            assertThat(managerMember.getRole()).isSameAs(MemberRole.ROLE_MANAGER);
        }
        for (Member normalMember : normalMembers) {
            assertThat(normalMember.getRole()).isSameAs(MemberRole.ROLE_NORMAL);
        }
    }

    @DisplayName("RunningNotice 에 참가한 모든 Member 반환 테스트")
    @Test
    public void findAllByRunningNoticeTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);

        for (int i = 1; i < 11; i++) {
            User tempUser = testEntityFactory.getUser(dongArea, i);
            Member tempMember = testEntityFactory.getMember(user, crew);
            testEntityFactory.getRunningMember(runningNotice, tempMember);
        }

        ///when
        List<Member> members = memberRepository.findAllByRunningNotice(runningNotice);

        //then
        assertThat(members.size()).isSameAs(10);
    }

}