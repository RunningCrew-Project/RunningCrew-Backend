package com.project.runningcrew.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.exception.notFound.FcmTokenNotFoundException;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.notification.entity.NotificationFactory;
import com.project.runningcrew.notification.entity.NotificationType;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;
import com.project.runningcrew.notification.entity.Notification;
import com.project.runningcrew.notification.repository.NotificationRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
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
                .map(f -> NotificationFactory.createNoticeBoardNotification(f.getUser(), crew, noticeBoard))
                .collect(Collectors.toList());
        notificationRepository.saveAllCustom(notifications);

        List<String> tokens = fcmTokenList.stream().map(FcmToken::getFcmToken).collect(Collectors.toList());
        sendMessages(tokens, NotificationType.NOTICE_BOARD.getTitle(),
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
                .map(f -> NotificationFactory.createRegularRunningNotification(f.getUser(), crew, runningNotice))
                .collect(Collectors.toList());
        notificationRepository.saveAllCustom(notifications);

        List<String> tokens = fcmTokenList.stream().map(FcmToken::getFcmToken).collect(Collectors.toList());
        sendMessages(tokens, NotificationType.REGULAR_RUNNING_NOTICE.getTitle(),
                runningNotice.getTitle(),
                crew.getCrewImgUrl(),
                NotificationType.REGULAR_RUNNING_NOTICE,
                runningNotice.getId());
    }

    /**
     * 크루에 가입되었다는 Notification 을 생성하고, 알림을 요청한다.
     *
     * @param crew     유저가 가입한 크루
     * @param joinUser 크루에 가입된 유저
     */
    public void sendCrewJoinMessage(Crew crew, User joinUser) {
        FcmToken fcmToken = fcmTokenRepository.findByUser(joinUser)
                .orElseThrow(FcmTokenNotFoundException::new);
        Notification notification = NotificationFactory.createCrewJoinNotification(joinUser, crew);
        notificationRepository.save(notification);

        sendMessage(fcmToken.getFcmToken(), NotificationType.CREW.getTitle(),
                notification.getContent(), crew.getCrewImgUrl(),
                NotificationType.CREW, crew.getId());
    }

    /**
     * 입력받은 토큰을 가진 기기에 알림을 보낸다.
     *
     * @param token            알림을 보낼 기기의 토큰
     * @param title            알림 제목
     * @param body             알림 내용
     * @param imgUrl           알림 이미지
     * @param notificationType 알림의 종류
     * @param id               알림을 만든 리소스의 id
     */
    private void sendMessage(String token, String title, String body, String imgUrl,
                             NotificationType notificationType, Long id) {
        Message message = Message.builder()
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setImage(imgUrl)
                                .build())
                .setToken(token)
                .putAllData(Map.of(
                        "type", notificationType.toString(),
                        "id", id.toString()
                ))
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("firebase messaging exception : {}", e);
        }
    }

    /**
     * 입력받은 토큰 리스트를 가진 기기들에게 알림을 보낸다.
     *
     * @param tokens           알림을 보낼 기기들의 토큰
     * @param title            알림 제목
     * @param body             알림 내용
     * @param imgUrl           알림 이미지
     * @param notificationType 알림의 종류
     * @param id               알림을 만든 리소스의 id
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
                        "type", notificationType.toString(),
                        "id", id.toString()
                ))
                .build();
        FirebaseMessaging.getInstance().sendMulticastAsync(messages);
    }

}
