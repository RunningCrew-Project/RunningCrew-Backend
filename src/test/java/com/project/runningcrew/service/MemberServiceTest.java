package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.notFound.MemberNotFoundException;
import com.project.runningcrew.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

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

    @DisplayName("멤버 저장 테스트")
    @Test
    public void saveTest(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);
        when(memberRepository.save(member)).thenReturn(member);

        ///when
        Long findMemberId = memberService.saveMember(member);

        //then
        assertThat(findMemberId).isSameAs(memberId);
        verify(memberRepository, times(1)).save(member);
    }

    @DisplayName("멤버 삭제 테스트")
    @Test
    public void deleteTest(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);
        doNothing().when(memberRepository).delete(member);

        ///when
        memberService.deleteMember(member);

        //then
        verify(memberRepository,times(1)).delete(member);
    }

    @DisplayName("멤버의 role 리더로 변경 테스트")
    @Test
    public void updateMemberLeaderTest(@Mock User user, @Mock Crew crew) {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, user, crew, MemberRole.ROLE_NORMAL);

        ///when
        memberService.updateMemberLeader(member);

        //then
        assertThat(member.getRole()).isSameAs(MemberRole.ROLE_LEADER);
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

}