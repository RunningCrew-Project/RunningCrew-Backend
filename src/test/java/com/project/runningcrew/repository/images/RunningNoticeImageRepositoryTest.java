package com.project.runningcrew.repository.images;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.resourceimage.entity.RunningNoticeImage;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.resourceimage.repository.RunningNoticeImageRepository;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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
        RunningNotice runningNotice = testEntityFactory.getRegularRunningNotice(member, 0);
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

    @DisplayName("runningNoticeId 의 리스트에 포함된 runningNoticeId 를 가진 RunningNoticeImage 반환 테스트")
    @Test
    public void findImagesByRunningNoticeIdsTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);

        RunningNotice runningNotice0 = testEntityFactory.getRegularRunningNotice(member, 0);
        for (int i = 0; i < 3; i++) {
            RunningNoticeImage runningNoticeImage =
                    new RunningNoticeImage("runningNoticeImage" + i, runningNotice0);
            runningNoticeImageRepository.save(runningNoticeImage);
        }

        RunningNotice runningNotice1 = testEntityFactory.getRegularRunningNotice(member, 1);
        for (int i = 0; i < 4; i++) {
            RunningNoticeImage runningNoticeImage =
                    new RunningNoticeImage("runningNoticeImage" + i, runningNotice1);
            runningNoticeImageRepository.save(runningNoticeImage);
        }

        RunningNotice runningNotice2 = testEntityFactory.getRegularRunningNotice(member, 2);
        for (int i = 0; i < 5; i++) {
            RunningNoticeImage runningNoticeImage =
                    new RunningNoticeImage("runningNoticeImage" + i, runningNotice2);
            runningNoticeImageRepository.save(runningNoticeImage);
        }

        List<Long> runningNoticeIds = List.of(
                runningNotice0.getId(), runningNotice1.getId(), runningNotice2.getId());

        ///when
        List<RunningNoticeImage> images = runningNoticeImageRepository.findImagesByRunningNoticeIds(runningNoticeIds);

        //then
        List<RunningNoticeImage> images0 = images.stream()
                .filter(runningNoticeImage -> runningNoticeImage.getRunningNotice().equals(runningNotice0))
                .collect(Collectors.toList());
        List<RunningNoticeImage> images1 = images.stream()
                .filter(runningNoticeImage -> runningNoticeImage.getRunningNotice().equals(runningNotice1))
                .collect(Collectors.toList());
        List<RunningNoticeImage> images2 = images.stream()
                .filter(runningNoticeImage -> runningNoticeImage.getRunningNotice().equals(runningNotice2))
                .collect(Collectors.toList());
        assertThat(images.size()).isSameAs(12);
        assertThat(images0.size()).isSameAs(3);
        assertThat(images1.size()).isSameAs(4);
        assertThat(images2.size()).isSameAs(5);
    }

}