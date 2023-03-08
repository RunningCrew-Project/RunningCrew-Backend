package com.project.runningcrew.refreshtoken.repository;

import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("RefreshToken 저장 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        RefreshToken refreshToken = new RefreshToken(user, "token");

        ///when
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        //then
        assertThat(savedRefreshToken).isEqualTo(refreshToken);
    }

    @DisplayName("id 로 RefreshToken 반환 성공 테스트")
    @Test
    public void findByIdTest1() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        RefreshToken refreshToken = new RefreshToken(user, "token");
        refreshTokenRepository.save(refreshToken);

        ///when
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository
                .findById(refreshToken.getId());

        //then
        assertThat(optionalRefreshToken).isNotEmpty();
        assertThat(optionalRefreshToken).hasValue(refreshToken);
    }

    @DisplayName("id 로 RefreshToken 반환 예외 테스트")
    @Test
    public void findByIdTest2() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Long refreshTokenId = 1L;

        ///when
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository
                .findById(refreshTokenId);

        //then
        assertThat(optionalRefreshToken).isEmpty();
    }

    @DisplayName("RefreshToken 삭제 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        RefreshToken refreshToken = new RefreshToken(user, "token");
        refreshTokenRepository.save(refreshToken);

        ///when
        refreshTokenRepository.delete(refreshToken);

        //then
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository
                .findById(refreshToken.getId());
        assertThat(optionalRefreshToken).isEmpty();
    }

}