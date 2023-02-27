package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RunningNoticeImageRepositoryTest {

    @Autowired
    RunningNoticeImageRepository runningNoticeImageRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("RunningNoticeImage save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage", runningNotice);

        ///when
        RunningNoticeImage savedImage = runningNoticeImageRepository.save(runningNoticeImage);

        //then
        assertThat(savedImage).isEqualTo(runningNoticeImage);
    }

    @DisplayName("RunningNoticeImage findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage", runningNotice);
        runningNoticeImageRepository.save(runningNoticeImage);

        ///when
        Optional<RunningNoticeImage> optRunningNoticeImage = runningNoticeImageRepository.findById(runningNoticeImage.getId());

        //then
        assertThat(optRunningNoticeImage).isNotEmpty();
        assertThat(optRunningNoticeImage).hasValue(runningNoticeImage);
    }

    @DisplayName("RunningNoticeImage delete 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage", runningNotice);
        runningNoticeImageRepository.save(runningNoticeImage);

        ///when
        runningNoticeImageRepository.delete(runningNoticeImage);

        //then
        Optional<RunningNoticeImage> optRunningNoticeImage = runningNoticeImageRepository.findById(runningNoticeImage.getId());
        assertThat(optRunningNoticeImage).isEmpty();
    }

    @DisplayName("RunningNoticeImage findAllByRunningNotice 테스트")
    @Test
    void findAllByRunningNoticeTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        for (int i = 0; i < 10; i++) {
            RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage" + i, runningNotice);
            runningNoticeImageRepository.save(runningNoticeImage);
        }

        //when
        List<RunningNoticeImage> runningNoticeImagesImages = runningNoticeImageRepository.findAllByRunningNotice(runningNotice);

        //then
        assertThat(runningNoticeImagesImages.size()).isSameAs(10);
        for (RunningNoticeImage runningNoticeImage : runningNoticeImagesImages) {
            assertThat(runningNoticeImage.getRunningNotice()).isEqualTo(runningNotice);
        }
    }

    @DisplayName("RunningNotice 에 포함된 모든 RunningNoticeImage 삭제")
    @Test
    public void deleteAllByRunningNoticeTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);
        for (int i = 0; i < 10; i++) {
            RunningNoticeImage runningNoticeImage = new RunningNoticeImage("runningNoticeImage" + i, runningNotice);
            runningNoticeImageRepository.save(runningNoticeImage);
        }

        ///when
        runningNoticeImageRepository.deleteAllByRunningNotice(runningNotice);

        //then
        List<RunningNoticeImage> images = runningNoticeImageRepository.findAllByRunningNotice(runningNotice);
        assertThat(images.isEmpty()).isTrue();
    }

}