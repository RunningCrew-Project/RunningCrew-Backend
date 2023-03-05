package com.project.runningcrew.fcm.token.repository;

import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.repository.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FcmTokenRepositoryTest {

    @Autowired
    FcmTokenRepository fcmTokenRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("FcmToken 저장 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        FcmToken token = new FcmToken(user, "token");

        ///when
        FcmToken savedToken = fcmTokenRepository.save(token);

        //then
        assertThat(savedToken).isEqualTo(token);
    }

    @DisplayName("FcmToken id 로 찾기 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        FcmToken token = new FcmToken(user, "token");
        fcmTokenRepository.save(token);

        ///when
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findById(token.getId());

        //then
        assertThat(optionalFcmToken).isNotEmpty();
        assertThat(optionalFcmToken).hasValue(token);
    }

    @DisplayName("FcmToken 삭제 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        FcmToken token = new FcmToken(user, "token");
        fcmTokenRepository.save(token);

        ///when
        fcmTokenRepository.delete(token);

        //then
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findById(token.getId());
        assertThat(optionalFcmToken).isEmpty();
    }

    @DisplayName("User 로 FcmToken 찾기 존재 테스트")
    @Test
    public void findByUserTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        FcmToken token = new FcmToken(user, "token");
        fcmTokenRepository.save(token);

        ///when
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findByUser(user);

        //then
        assertThat(optionalFcmToken).isNotEmpty();
        assertThat(optionalFcmToken).hasValue(token);
    }

    @DisplayName("User 로 FcmToken 찾기 존재 안함 테스트")
    @Test
    public void findByUserTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);

        ///when
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findByUser(user);

        //then
        assertThat(optionalFcmToken).isEmpty();
    }

    @DisplayName("User 를 가지는 FcmToken 존재 true 테스트")
    @Test
    public void existsByUserTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        FcmToken token = new FcmToken(user, "token");
        fcmTokenRepository.save(token);

        ///when
        boolean result = fcmTokenRepository.existsByUser(user);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("User 를 가지는 FcmToken 존재 false 테스트")
    @Test
    public void existsByUserTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);

        ///when
        boolean result = fcmTokenRepository.existsByUser(user);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("User 의 id 리스트로 FcmToken 모두 찾기 테스트")
    @Test
    public void findAllByUserIdsTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = testEntityFactory.getUser(dongArea, i);
            FcmToken token = new FcmToken(user, "token" + i);
            fcmTokenRepository.save(token);
            users.add(user);
        }
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());

        ///when
        List<FcmToken> fcmTokens = fcmTokenRepository.findAllByUserIds(userIds);

        //then
        List<Long> result = fcmTokens.stream().map(f -> f.getUser().getId()).collect(Collectors.toList());
        assertThat(fcmTokens.size()).isSameAs(userIds.size());
        assertThat(result.equals(userIds)).isTrue();
    }

}