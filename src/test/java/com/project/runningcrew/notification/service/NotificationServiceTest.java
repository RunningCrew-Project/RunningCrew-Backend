package com.project.runningcrew.notification.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.notification.entity.NotificationFactory;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.notification.entity.Notification;
import com.project.runningcrew.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    NotificationRepository notificationRepository;

    @InjectMocks
    NotificationService notificationService;

    @DisplayName("User 의 Notification 페이징하여 반환 첫 페이지 테스트")
    @Test
    public void findByUserTest1(@Mock User user, @Mock Crew crew) {
        //given
        Member member = new Member(user, crew, MemberRole.ROLE_LEADER);
        PageRequest pageRequest = PageRequest.of(0, 7);
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            NoticeBoard noticeBoard = new NoticeBoard(member, "title" + i, "content" + i);
            Notification notification = NotificationFactory.createNoticeBoardNotification(user, crew, noticeBoard);
            notifications.add(notification);
        }
        SliceImpl<Notification> notificationSlice = new SliceImpl<>(notifications, pageRequest, true);
        when(notificationRepository.findByUser(user, pageRequest)).thenReturn(notificationSlice);

        ///when
        Slice<Notification> result = notificationService.findByUser(user, pageRequest);

        //then
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        verify(notificationRepository, times(1)).findByUser(user, pageRequest);
    }

    @DisplayName("User 의 Notification 페이징하여 반환 마지막 페이지 테스트")
    @Test
    public void findByUserTest2(@Mock User user, @Mock Crew crew) {
        //given
        Member member = new Member(user, crew, MemberRole.ROLE_LEADER);
        PageRequest pageRequest = PageRequest.of(1, 7);
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NoticeBoard noticeBoard = new NoticeBoard(member, "title" + i, "content" + i);
            Notification notification = NotificationFactory.createNoticeBoardNotification(user, crew, noticeBoard);
            notifications.add(notification);
        }
        SliceImpl<Notification> notificationSlice = new SliceImpl<>(notifications, pageRequest, false);
        when(notificationRepository.findByUser(user, pageRequest)).thenReturn(notificationSlice);

        ///when
        Slice<Notification> result = notificationService.findByUser(user, pageRequest);

        //then
        assertThat(result.getNumber()).isSameAs(1);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(3);
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.hasNext()).isFalse();
        assertThat(result.isLast()).isTrue();
        verify(notificationRepository, times(1)).findByUser(user, pageRequest);
    }

}