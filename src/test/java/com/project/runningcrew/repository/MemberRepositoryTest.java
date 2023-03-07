package com.project.runningcrew.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

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
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);

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

    @DisplayName("user 가 crew 가입함 테스트")
    @Test
    public void existsByUserAndCrew1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);

        ///when
        boolean result = memberRepository.existsByUserAndCrew(user, crew);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("user 가 crew 가입 안함 테스트")
    @Test
    public void existsByUserAndCrew2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);

        ///when
        boolean result = memberRepository.existsByUserAndCrew(user, crew);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("user 와 crew 가진 member 존재 테스트")
    @Test
    public void findByUserAndCrewTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);

        ///when
        Optional<Member> optMember = memberRepository.findByUserAndCrew(user, crew);

        //then
        assertThat(optMember).isNotEmpty();
        assertThat(optMember).hasValue(member);
    }

    @DisplayName("user 와 crew 가진 member 존재 안함 테스트")
    @Test
    public void findByUserAndCrewTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);

        ///when
        Optional<Member> optMember = memberRepository.findByUserAndCrew(user, crew);

        //then
        assertThat(optMember).isEmpty();
    }

    @DisplayName("crewId 의 리스트에 담긴 id 를 가지는 크루들의 멤버수 반환 테스트")
    @Test
    public void countAllByCrewIdsTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);

        Crew crew0 = testEntityFactory.getCrew(dongArea, 0);
        for (int i = 0; i < 10; i++) {
            User user = testEntityFactory.getUser(dongArea, i);
            Member member = new Member(user, crew0, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        Crew crew1 = testEntityFactory.getCrew(dongArea, 1);
        for (int i = 10; i < 30; i++) {
            User user = testEntityFactory.getUser(dongArea, i);
            Member member = new Member(user, crew1, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        Crew crew2 = testEntityFactory.getCrew(dongArea, 2);
        for (int i = 30; i < 60; i++) {
            User user = testEntityFactory.getUser(dongArea, i);
            Member member = new Member(user, crew2, MemberRole.ROLE_NORMAL);
            memberRepository.save(member);
        }

        List<Long> crewIds = List.of(crew0.getId(), crew1.getId(), crew2.getId());

        ///when
        List<Object[]> memberCounts = memberRepository.countAllByCrewIds(crewIds);

        //then
        Map<Long, Long> commentMaps = memberCounts.stream()
                .collect(Collectors.toMap(o -> (Long) o[0], o -> (Long) o[1]));
        assertThat(commentMaps.get(crew0.getId())).isSameAs(10L);
        assertThat(commentMaps.get(crew1.getId())).isSameAs(20L);
        assertThat(commentMaps.get(crew2.getId())).isSameAs(30L);
    }

}