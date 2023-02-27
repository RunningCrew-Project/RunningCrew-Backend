package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RunningRecordImageRepositoryTest {

    @Autowired
    RunningRecordImageRepository runningRecordImageRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("RunningRecordImage save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = testEntityFactory.getPersonalRunningRecord(user, 0);
        RunningRecordImage runningRecordImage = new RunningRecordImage("fileName", personalRunningRecord);

        ///when
        RunningRecordImage savedRunningRecordImage = runningRecordImageRepository.save(runningRecordImage);

        //then
        assertThat(savedRunningRecordImage).isEqualTo(runningRecordImage);
    }

    @DisplayName("RunningRecordImage findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = testEntityFactory.getPersonalRunningRecord(user, 0);
        RunningRecordImage runningRecordImage = new RunningRecordImage("fileName", personalRunningRecord);
        runningRecordImageRepository.save(runningRecordImage);

        ///when
        Optional<RunningRecordImage> optRunningRecordImage = runningRecordImageRepository.findById(runningRecordImage.getId());

        //then
        assertThat(optRunningRecordImage).isNotEmpty();
        assertThat(optRunningRecordImage).hasValue(runningRecordImage);
    }

    @DisplayName("RunningRecordImage delete 테스트")
    @Test
    public void deleteTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = testEntityFactory.getPersonalRunningRecord(user, 0);
        RunningRecordImage runningRecordImage = new RunningRecordImage("fileName", personalRunningRecord);
        runningRecordImageRepository.save(runningRecordImage);

        ///when
        runningRecordImageRepository.delete(runningRecordImage);

        //then
        Optional<RunningRecordImage> optRunningRecordImage = runningRecordImageRepository.findById(runningRecordImage.getId());
        assertThat(optRunningRecordImage).isEmpty();
    }

    @DisplayName("RunningRecordImage findAllByRunningRecord 테스트")
    @Test
    public void findAllByRunningRecordTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = testEntityFactory.getPersonalRunningRecord(user, 0);
        for (int i = 0; i < 10; i++) {
            RunningRecordImage runningRecordImage = new RunningRecordImage("fileName" + i, personalRunningRecord);
            runningRecordImageRepository.save(runningRecordImage);
        }

        ///when
        List<RunningRecordImage> runningRecordImages = runningRecordImageRepository.findAllByRunningRecord(personalRunningRecord);

        //then
        assertThat(runningRecordImages.size()).isSameAs(10);
        for (RunningRecordImage runningRecordImage : runningRecordImages) {
            assertThat(runningRecordImage.getRunningRecord()).isEqualTo(personalRunningRecord);
        }
    }

    @DisplayName("RunningRecord 에 포함된 모든 RunningRecordImage 삭제")
    @Test
    public void deleteAllByRunningRecordTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = testEntityFactory.getPersonalRunningRecord(user, 0);
        for (int i = 0; i < 10; i++) {
            RunningRecordImage runningRecordImage = new RunningRecordImage("fileName" + i, personalRunningRecord);
            runningRecordImageRepository.save(runningRecordImage);
        }

        ///when
        runningRecordImageRepository.deleteAllByRunningRecord(personalRunningRecord);

        //then
        List<RunningRecordImage> images = runningRecordImageRepository.findAllByRunningRecord(personalRunningRecord);
        assertThat(images.isEmpty()).isTrue();
    }

}