package com.project.runningcrew.notification.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.notification.entity.Notification;
import com.project.runningcrew.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    TestEntityFactory testEntityFactory;


    @DisplayName("공지글 Notification 저장 테스트")
    @Test
    public void saveTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        NoticeBoard noticeBoard = testEntityFactory.getNoticeBoard(member, 0);
        Notification notification = Notification.createNoticeBoardNotification(user, crew, noticeBoard);

        ///when
        Notification savedNotification = notificationRepository.save(notification);

        //then
        assertThat(savedNotification).isEqualTo(notification);
    }

    @DisplayName("정기런닝 Notification 저장 테스트")
    @Test
    public void saveTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice regularRunningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
        Notification notification = Notification.createRegularRunningNotification(user, crew, regularRunningNotice);

        ///when
        Notification savedNotification = notificationRepository.save(notification);

        //then
        assertThat(savedNotification).isEqualTo(notification);
    }

    @DisplayName("User 의 Notification 페이징하여 반환 첫 페이지 테스트")
    @Test
    public void findByUserTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        for (int i = 0; i < 10; i++) {
            NoticeBoard noticeBoard = testEntityFactory.getNoticeBoard(member, 0);
            Notification notification = Notification.createNoticeBoardNotification(user, crew, noticeBoard);
            notificationRepository.save(notification);
        }

        for (int i = 0; i < 10; i++) {
            RunningNotice regularRunningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
            Notification notification = Notification.createRegularRunningNotification(user, crew, regularRunningNotice);
            notificationRepository.save(notification);
        }

        ///when
        PageRequest pageRequest = PageRequest.of(0, 12);
        Slice<Notification> notifications = notificationRepository.findByUser(user, pageRequest);

        //then
        assertThat(notifications.getNumber()).isSameAs(0);
        assertThat(notifications.getSize()).isSameAs(12);
        assertThat(notifications.getNumberOfElements()).isSameAs(12);
        assertThat(notifications.hasPrevious()).isFalse();
        assertThat(notifications.hasNext()).isTrue();
        assertThat(notifications.isFirst()).isTrue();
    }

    @DisplayName("User 의 Notification 페이징하여 반환 마지막 페이지 테스트")
    @Test
    public void findByUserTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        for (int i = 0; i < 10; i++) {
            NoticeBoard noticeBoard = testEntityFactory.getNoticeBoard(member, 0);
            Notification notification = Notification.createNoticeBoardNotification(user, crew, noticeBoard);
            notificationRepository.save(notification);
        }

        for (int i = 0; i < 10; i++) {
            RunningNotice regularRunningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
            Notification notification = Notification.createRegularRunningNotification(user, crew, regularRunningNotice);
            notificationRepository.save(notification);
        }

        ///when
        PageRequest pageRequest = PageRequest.of(1, 12);
        Slice<Notification> notifications = notificationRepository.findByUser(user, pageRequest);

        //then
        assertThat(notifications.getNumber()).isSameAs(1);
        assertThat(notifications.getSize()).isSameAs(12);
        assertThat(notifications.getNumberOfElements()).isSameAs(8);
        assertThat(notifications.hasPrevious()).isTrue();
        assertThat(notifications.hasNext()).isFalse();
        assertThat(notifications.isLast()).isTrue();
    }

}