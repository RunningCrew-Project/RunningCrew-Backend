package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.runningrecord.entity.CrewRunningRecord;
import com.project.runningcrew.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CrewRunningRecordRepositoryTest {

    @Autowired
    CrewRunningRecordRepository crewRunningRecordRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("saveTest 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);

        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .title("crew")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();

        ///when
        CrewRunningRecord savedCrewRunningRecord = crewRunningRecordRepository.save(crewRunningRecord);

        //then
        assertThat(savedCrewRunningRecord).isEqualTo(crewRunningRecord);
    }

    @DisplayName("findById 테스트")
    @Test
    public void findById() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);

        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .title("crew")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        crewRunningRecordRepository.save(crewRunningRecord);

        ///when
        Optional<CrewRunningRecord> optCrewRunningRecord = crewRunningRecordRepository.findById(crewRunningRecord.getId());

        //then
        assertThat(optCrewRunningRecord).isNotEmpty();
        assertThat(optCrewRunningRecord).hasValue(crewRunningRecord);
    }

    @DisplayName("delete 테스트")
    @Test
    public void delete() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);

        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .title("crew")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        crewRunningRecordRepository.save(crewRunningRecord);

        ///when
        crewRunningRecordRepository.delete(crewRunningRecord);

        //then
        Optional<CrewRunningRecord> optCrewRunningRecord = crewRunningRecordRepository.findById(crewRunningRecord.getId());
        assertThat(optCrewRunningRecord).isEmpty();
    }

}