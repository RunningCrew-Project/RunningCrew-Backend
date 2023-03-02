package com.project.runningcrew.service;

import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.exception.RunningDateTimeException;
import com.project.runningcrew.exception.RunningPersonnelException;
import com.project.runningcrew.exception.alreadyExist.RunningMemberAlreadyExistsException;
import com.project.runningcrew.exception.notFound.RunningMemberNotFoundException;
import com.project.runningcrew.repository.RunningMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningMemberServiceTest {

    @Mock
    private RunningMemberRepository runningMemberRepository;

    @InjectMocks
    private RunningMemberService runningMemberService;

    @DisplayName("런닝 참여 성공 테스트")
    @Test
    public void saveRunningMemberTest1(@Mock Member member) {
        //given
        Long runningMemberId = 1L;
        RunningNotice runningNotice = RunningNotice.builder()
                .id(runningMemberId)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(LocalDateTime.now().plusDays(1))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();
        RunningMember runningMember = new RunningMember(runningMemberId, runningNotice, member);
        when(runningMemberRepository.countAllByRunningNotice(runningNotice)).thenReturn(5L);
        when(runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice))
                .thenReturn(false);
        when(runningMemberRepository.save(any())).thenReturn(runningMember);

        ///when
        Long saveId = runningMemberService.saveRunningMember(member, runningNotice);

        //then
        assertThat(saveId).isSameAs(runningMemberId);
        verify(runningMemberRepository, times(1)).existsByMemberAndRunningNotice(member, runningNotice);
        verify(runningMemberRepository, times(1)).save(any());
    }

    @DisplayName("런닝 참여 신청 시간 예외 테스트")
    @Test
    public void saveRunningMemberTest2(@Mock Member member) {
        //given
        Long runningMemberId = 1L;
        RunningNotice runningNotice = RunningNotice.builder()
                .id(runningMemberId)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(LocalDateTime.now().minusDays(1))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();

        ///when
        //then
        assertThatThrownBy(() -> runningMemberService.saveRunningMember(member, runningNotice))
                .isInstanceOf(RunningDateTimeException.class);
    }

    @DisplayName("런닝 참여 신청 인원 초과 예외 테스트")
    @Test
    public void saveRunningMemberTest3(@Mock Member member) {
        //given
        Long runningMemberId = 1L;
        RunningNotice runningNotice = RunningNotice.builder()
                .id(runningMemberId)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(LocalDateTime.now().plusDays(1))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();
        when(runningMemberRepository.countAllByRunningNotice(runningNotice)).thenReturn(11L);

        ///when
        //then
        assertThatThrownBy(() -> runningMemberService.saveRunningMember(member, runningNotice))
                .isInstanceOf(RunningPersonnelException.class);
        verify(runningMemberRepository, times(1)).countAllByRunningNotice(runningNotice);
    }

    @DisplayName("런닝 참여 신청 중복 예외 테스트")
    @Test
    public void saveRunningMemberTest4(@Mock Member member) {
        //given
        Long runningMemberId = 1L;
        RunningNotice runningNotice = RunningNotice.builder()
                .id(runningMemberId)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(LocalDateTime.now().plusDays(1))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();
        when(runningMemberRepository.countAllByRunningNotice(runningNotice)).thenReturn(8L);
        when(runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice))
                .thenReturn(true);

        ///when
        //then
        assertThatThrownBy(() -> runningMemberService.saveRunningMember(member, runningNotice))
                .isInstanceOf(RunningMemberAlreadyExistsException.class);
        verify(runningMemberRepository, times(1)).existsByMemberAndRunningNotice(member, runningNotice);
    }

    @DisplayName("런닝 참여 취소 성공 테스트")
    @Test
    public void deleteRunningMemberTest1(@Mock Member member, @Mock RunningNotice runningNotice) {
        //given
        RunningMember runningMember = new RunningMember(runningNotice, member);
        when(runningMemberRepository.findByMemberAndRunningNotice(member, runningNotice))
                .thenReturn(Optional.of(runningMember));
        doNothing().when(runningMemberRepository).delete(runningMember);

        ///when
        runningMemberService.deleteRunningMember(member, runningNotice);

        //then
        verify(runningMemberRepository, times(1)).findByMemberAndRunningNotice(member, runningNotice);
        verify(runningMemberRepository, times(1)).delete(runningMember);
    }

    @DisplayName("런닝 참여 취소 신청 시간 예외 테스트")
    @Test
    public void deleteRunningMemberTest2(@Mock Member member) {
        //given
        Long runningMemberId = 1L;
        RunningNotice runningNotice = RunningNotice.builder()
                .id(runningMemberId)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.INSTANT)
                .runningDateTime(LocalDateTime.now().minusDays(1))
                .runningPersonnel(10)
                .status(RunningStatus.START)
                .build();

        ///when
        //then
        assertThatThrownBy(() -> runningMemberService.deleteRunningMember(member, runningNotice))
                .isInstanceOf(RunningDateTimeException.class);
    }

    @DisplayName("런닝 참여 안한 멤버가 취소하는 예외 테스트")
    @Test
    public void deleteRunningMemberTest3(@Mock Member member, @Mock RunningNotice runningNotice) {
        //given
        when(runningMemberRepository.findByMemberAndRunningNotice(member, runningNotice))
                .thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> runningMemberService.deleteRunningMember(member, runningNotice))
                .isInstanceOf(RunningMemberNotFoundException.class);
        verify(runningMemberRepository, times(1)).findByMemberAndRunningNotice(member, runningNotice);
    }

    @DisplayName("런닝공지 참여 멤버수 반환 테스트")
    @Test
    public void countAllByRunningNoticeTest(@Mock RunningNotice runningNotice) {
        //given
        Long count = 10L;
        when(runningMemberRepository.countAllByRunningNotice(runningNotice)).thenReturn(count);

        ///when
        Long result = runningMemberService.countAllByRunningNotice(runningNotice);

        //then
        assertThat(result).isSameAs(count);
        verify(runningMemberRepository, times(1)).countAllByRunningNotice(runningNotice);
    }

    @DisplayName("멤버의 런닝공지 참여 true 테스트")
    @Test
    public void existsByMemberAndRunningNoticeTest1(@Mock Member member, @Mock RunningNotice runningNotice) {
        //given
        when(runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice)).thenReturn(true);

        ///when
        boolean result = runningMemberService.existsByMemberAndRunningNotice(member, runningNotice);

        //then
        assertThat(result).isTrue();
        verify(runningMemberRepository, times(1))
                .existsByMemberAndRunningNotice(member, runningNotice);
    }

    @DisplayName("멤버의 런닝공지 참여 false 테스트")
    @Test
    public void existsByMemberAndRunningNoticeTest2(@Mock Member member, @Mock RunningNotice runningNotice) {
        //given
        when(runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice)).thenReturn(false);

        ///when
        boolean result = runningMemberService.existsByMemberAndRunningNotice(member, runningNotice);

        //then
        assertThat(result).isFalse();
        verify(runningMemberRepository, times(1))
                .existsByMemberAndRunningNotice(member, runningNotice);
    }

}