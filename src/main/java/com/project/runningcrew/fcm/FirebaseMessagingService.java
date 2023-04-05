package com.project.runningcrew.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.notification.entity.NotificationType;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;
import com.project.runningcrew.notification.entity.Notification;
import com.project.runningcrew.notification.entity.NotificationTitle;
import com.project.runningcrew.notification.repository.NotificationRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;

    /**
     * noticeBoard 에 대한 Notification 들을 생성하고, 알림을 요청한다.
     *
     * @param crew        알림을 보낼 크루
     * @param noticeBoard 생성 알림을 보내는 NoticeBoard
     */
    @Transactional
    public void sendNoticeBoardMessages(Crew crew, NoticeBoard noticeBoard) {
        List<Long> userIds = memberRepository.findAllByCrew(crew).stream()
                .map(Member::getUser)
                .map(User::getId)
                .collect(Collectors.toList());

        List<FcmToken> fcmTokenList = fcmTokenRepository.findAllByUserIds(userIds);
        List<Notification> notifications = fcmTokenList.stream()
                .map(f -> Notification.createNoticeBoardNotification(f.getUser(), crew, noticeBoard))
                .collect(Collectors.toList());
        notificationRepository.saveAllCustom(notifications);

        List<String> tokens = fcmTokenList.stream().map(FcmToken::getFcmToken).collect(Collectors.toList());
        sendMessages(tokens, NotificationTitle.NEW_NOTICE_BOARD_TITLE,
                noticeBoard.getTitle(),
                crew.getCrewImgUrl(),
                NotificationType.NOTICE_BOARD,
                noticeBoard.getId());

    }

    /**
     * noticeType 이 REGULAR 인 RunningNotice 에 대한 Notification 들을 생성하고, 알림을 요청한다.
     *
     * @param crew          알림을 보낼 크루
     * @param runningNotice 생성 알림을 보내는 RunningNotice
     */
    @Transactional
    public void sendRegularRunningNoticeMessages(Crew crew, RunningNotice runningNotice) {
        if (runningNotice.getNoticeType() != NoticeType.REGULAR) {
            return;
        }

        List<Long> userIds = memberRepository.findAllByCrew(crew).stream()
                .map(Member::getUser)
                .map(User::getId)
                .collect(Collectors.toList());

        List<FcmToken> fcmTokenList = fcmTokenRepository.findAllByUserIds(userIds);
        List<Notification> notifications = fcmTokenList.stream()
                .map(f -> Notification.createRegularRunningNotification(f.getUser(), crew, runningNotice))
                .collect(Collectors.toList());
        notificationRepository.saveAllCustom(notifications);

        List<String> tokens = fcmTokenList.stream().map(FcmToken::getFcmToken).collect(Collectors.toList());
        sendMessages(tokens, NotificationTitle.NEW_REGULAR_RUNNING_NOTICE_TITLE,
                runningNotice.getTitle(),
                crew.getCrewImgUrl(),
                NotificationType.REGULAR_RUNNING_NOTICE,
                runningNotice.getId());
    }

    /**
     * 입력받은 토큰 리스트를 가진 기기들에게 알림을 보낸다.
     *
     * @param tokens 알림을 보낼 기기들의 토큰
     * @param title  알림 제목
     * @param body   알림 내용
     * @param imgUrl 알림 이미지
     */
    private void sendMessages(List<String> tokens, String title, String body, String imgUrl,
                              NotificationType notificationType, Long id) {
        MulticastMessage messages = MulticastMessage.builder()
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setImage(imgUrl)
                                .build())
                .addAllTokens(tokens)
                .putAllData(Map.of(
                        "type",notificationType.toString(),
                        "id", id.toString()
                ))
                .build();
        FirebaseMessaging.getInstance().sendMulticastAsync(messages);
    }

}
