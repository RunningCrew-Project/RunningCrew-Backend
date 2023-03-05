package com.project.runningcrew.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;
import com.project.runningcrew.notification.entity.Notification;
import com.project.runningcrew.notification.entity.NotificationTitle;
import com.project.runningcrew.notification.repository.NotificationRepository;
import com.project.runningcrew.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;

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
        notificationRepository.saveAll(notifications);

        List<String> tokens = fcmTokenList.stream().map(FcmToken::getFcmToken).collect(Collectors.toList());
        sendMessages(tokens, NotificationTitle.NEW_NOTICE_BOARD_TITLE,
                noticeBoard.getTitle(),
                crew.getCrewImgUrl());

    }

    @Transactional
    public void sendRegularRunningNoticeMessages(Crew crew, RunningNotice runningNotice) {
        List<Long> userIds = memberRepository.findAllByCrew(crew).stream()
                .map(Member::getUser)
                .map(User::getId)
                .collect(Collectors.toList());

        List<FcmToken> fcmTokenList = fcmTokenRepository.findAllByUserIds(userIds);
        List<Notification> notifications = fcmTokenList.stream()
                .map(f -> Notification.createRegularRunningNotification(f.getUser(), crew, runningNotice))
                .collect(Collectors.toList());
        notificationRepository.saveAll(notifications);

        List<String> tokens = fcmTokenList.stream().map(FcmToken::getFcmToken).collect(Collectors.toList());
        sendMessages(tokens, NotificationTitle.NEW_REGULAR_RUNNING_NOTICE_TITLE,
                runningNotice.getTitle(),
                crew.getCrewImgUrl());
    }

    private void sendMessages(List<String> tokens, String title, String body, String imgUrl) {
        MulticastMessage messages = MulticastMessage.builder()
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .setImage(imgUrl)
                                .build())
                .addAllTokens(tokens)
                .build();
        FirebaseMessaging.getInstance().sendMulticastAsync(messages);
    }

}
