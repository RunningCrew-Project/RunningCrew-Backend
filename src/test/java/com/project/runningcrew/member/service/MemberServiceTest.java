package com.project.runningcrew.member.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.fcm.FirebaseMessagingService;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.recruitanswer.repository.RecruitAnswerRepository;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.duplicate.MemberDuplicateException;
import com.project.runningcrew.exception.notFound.MemberNotFoundException;
import com.project.runningcrew.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RecruitAnswerRepository recruitAnswerRepository;

    @Mock
    private FirebaseMessagingService firebaseMessagingService;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("Id 로 멤버 하나 가져오기 성공 테스트")
    @Test
    public void findByIdTest1(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        ///when
        Member findMember = memberService.findById(memberId);

        //then
        assertThat(findMember).isEqualTo(member);
        verify(memberRepository, times(1)).findById(memberId);
    }

    @DisplayName("Id 로 멤버 하나 가져오기 예외 테스트")
    @Test
    public void findByIdTest2(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> memberService.findById(memberId))
                .isInstanceOf(MemberNotFoundException.class);
        verify(memberRepository, times(1)).findById(memberId);
    }

    @DisplayName("멤버 저장 성공 테스트")
    @Test
    public void saveTest1(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);
        when(memberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.empty());
        when(memberRepository.save(member)).thenReturn(member);

        ///when
        Long findMemberId = memberService.saveMember(member);

        //then
        assertThat(findMemberId).isSameAs(memberId);
        verify(memberRepository, times(1)).findByUserAndCrew(user, crew);
        verify(memberRepository, times(1)).save(member);
    }

    @DisplayName("멤버 저장 예외 테스트")
    @Test
    public void saveTest2(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);
        when(memberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.of(member));

        ///when
        //then
        assertThatThrownBy(() -> memberService.saveMember(member))
                .isInstanceOf(MemberDuplicateException.class);
        verify(memberRepository, times(1)).findByUserAndCrew(user, crew);
    }

    @DisplayName("멤버 가입 수락 성공 테스트")
    @Test
    public void acceptMemberTest1(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);
        when(memberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.empty());
        doNothing().when(firebaseMessagingService).sendCrewJoinMessage(crew, user);
        doNothing().when(recruitAnswerRepository).deleteByUserAndCrew(user, crew);
        when(memberRepository.save(member)).thenReturn(member);

        ///when
        Long findMemberId = memberService.acceptMember(member);

        //then
        assertThat(findMemberId).isSameAs(memberId);
        verify(memberRepository, times(1)).findByUserAndCrew(user, crew);
        verify(firebaseMessagingService, times(1)).sendCrewJoinMessage(crew, user);
        verify(recruitAnswerRepository,times(1)).deleteByUserAndCrew(user, crew);
        verify(memberRepository, times(1)).save(member);
    }

//    @DisplayName("멤버 삭제 테스트")
//    @Test
//    public void deleteTest(@Mock User user, @Mock Crew crew) {
//        //given
//        Long memberId = 1L;
//        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);
//        doNothing().when(memberRepository).delete(member);
//
//        ///when
//        memberService.deleteMember(member);
//
//        //then
//        verify(memberRepository, times(1)).delete(member);
//    }

    @DisplayName("멤버의 role 리더로 변경 테스트")
    @Test
    public void updateMemberLeaderTest(@Mock User user1, @Mock User user2, @Mock Crew crew) {
        //given
        Long memberId1 = 1L;
        Long memberId2 = 1L;
        Member leaderMember = new Member(memberId1, user1, crew, MemberRole.ROLE_LEADER);
        Member updateMember = new Member(memberId2, user2, crew, MemberRole.ROLE_MANAGER);

        ///when
        memberService.updateMemberLeader(leaderMember, updateMember);

        //then
        assertThat(leaderMember.getRole()).isSameAs(MemberRole.ROLE_MANAGER);
        assertThat(updateMember.getRole()).isSameAs(MemberRole.ROLE_LEADER);
    }

    @DisplayName("멤버의 role 매니저로 변경 테스트")
    @Test
    public void updateMemberManagerTest(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);

        ///when
        memberService.updateMemberManager(member);

        //then
        assertThat(member.getRole()).isSameAs(MemberRole.ROLE_MANAGER);
    }

    @DisplayName("멤버의 role 일반 멤버로 변경 테스트")
    @Test
    public void updateMemberNormalTest(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_MANAGER);

        ///when
        memberService.updateMemberNormal(member);

        //then
        assertThat(member.getRole()).isSameAs(MemberRole.ROLE_NORMAL);
    }

    @DisplayName("유저에 속한 모든 멤버 가져오기 테스트")
    @Test
    public void findAllByUserTest(@Mock User user) {
        //given
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Crew crew = Crew.builder().build();
            members.add(new Member(user, crew, MemberRole.ROLE_NORMAL));
        }
        when(memberRepository.findAllByUser(user)).thenReturn(members);

        ///when
        List<Member> memberList = memberService.findAllByUser(user);

        //then
        assertThat(memberList.size()).isSameAs(members.size());
        for (Member member : memberList) {
            assertThat(member.getUser()).isEqualTo(user);
        }
        verify(memberRepository, times(1)).findAllByUser(user);
    }

    @DisplayName("크루에 속한 모든 멤버 수 테스트")
    @Test
    public void countAllByCrewTest(@Mock Crew crew) {
        //given
        Long memberNum = 10L;
        when(memberRepository.countAllByCrew(crew)).thenReturn(memberNum);

        ///when
        Long count = memberService.countAllByCrew(crew);

        //then
        assertThat(count).isSameAs(memberNum);
        verify(memberRepository, times(1)).countAllByCrew(crew);
    }

    @DisplayName("크루에 속한 모든 멤버 반환 테스트")
    @Test
    public void findAllByCrewText(@Mock Crew crew) {
        //given
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder().build();
            members.add(new Member(user, crew, MemberRole.ROLE_NORMAL));
        }
        when(memberRepository.findAllByCrew(crew)).thenReturn(members);

        ///when
        List<Member> memberList = memberService.findAllByCrew(crew);

        //then
        assertThat(memberList.size()).isSameAs(members.size());
        for (Member member : memberList) {
            assertThat(member.getCrew()).isEqualTo(crew);
        }
        verify(memberRepository, times(1)).findAllByCrew(crew);
    }

    @DisplayName("크루에 속한 관리자 멤버 반환 테스트")
    @Test
    public void findCrewMangersTest(@Mock Crew crew) {
        //given
        List<Member> leaders = new ArrayList<>();
        List<Member> managers = new ArrayList<>();
        User user = User.builder().build();
        leaders.add(new Member(user, crew, MemberRole.ROLE_LEADER));
        for (int i = 0; i < 10; i++) {
            User tempUser = User.builder().build();
            managers.add(new Member(tempUser, crew, MemberRole.ROLE_MANAGER));
        }

        when(memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_LEADER)).thenReturn(leaders);
        when(memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_MANAGER)).thenReturn(managers);

        ///when
        List<Member> crewMangers = memberService.findCrewMangers(crew);

        //then
        for (Member crewManger : crewMangers) {
            assertThat(crewManger.getRole()).isIn(MemberRole.ROLE_LEADER, MemberRole.ROLE_MANAGER);
        }
        verify(memberRepository, times(1)).findAllByCrewAndRole(crew, MemberRole.ROLE_LEADER);
        verify(memberRepository, times(1)).findAllByCrewAndRole(crew, MemberRole.ROLE_MANAGER);
    }

    @DisplayName("런닝공지에 참가한 모든 멤버 반환 테스트")
    @Test
    public void findAllByRunningNoticeTest(@Mock Crew crew, @Mock RunningNotice runningNotice) {
        //given
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member(User.builder().build(), crew, MemberRole.ROLE_NORMAL);
            members.add(member);
        }

        when(memberRepository.findAllByRunningNotice(runningNotice)).thenReturn(members);

        ///when
        List<Member> memberList = memberService.findAllByRunningNotice(runningNotice);

        //then
        assertThat(memberList.size()).isSameAs(10);
        verify(memberRepository, times(1)).findAllByRunningNotice(runningNotice);
    }

    @DisplayName("특정 유저와 특정 크루에 해당하는 멤버 반환 테스트")
    @Test
    public void findByUserAndCrewTest1(@Mock User user, @Mock Crew crew) {
        //given
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        when(memberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.of(member));

        ///when
        Member findMember = memberService.findByUserAndCrew(user, crew);

        //then
        assertThat(findMember).isEqualTo(member);
        verify(memberRepository, times(1)).findByUserAndCrew(user, crew);
    }

    @DisplayName("특정 유저와 특정 크루에 해당하는 멤버 예외 테스트")
    @Test
    public void findByUserAndCrewTest2(@Mock User user, @Mock Crew crew) {
        //given
        when(memberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> memberService.findByUserAndCrew(user, crew))
                .isInstanceOf(MemberNotFoundException.class);
        verify(memberRepository, times(1)).findByUserAndCrew(user, crew);
    }

    @DisplayName("크루 id 리스트를 받아 크루 id 와 크루 멤버수의 map 반환")
    @Test
    public void countAllByCrewIdsTest() {
        //given
        List<Long> crewIds = List.of(1L, 2L, 3L);
        List<Object[]> crewMemberList = List.of(
                new Object[]{1L, 10L},
                new Object[]{2L, 20L},
                new Object[]{3L, 30L}
        );
        when(memberRepository.countAllByCrewIds(crewIds)).thenReturn(crewMemberList);

        ///when
        Map<Long, Long> crewMemberMap = memberService.countAllByCrewIds(crewIds);

        //then
        assertThat(crewMemberMap.get(1L)).isSameAs(10L);
        assertThat(crewMemberMap.get(2L)).isSameAs(20L);
        assertThat(crewMemberMap.get(3L)).isSameAs(30L);
        verify(memberRepository, times(1)).countAllByCrewIds(crewIds);
    }

}