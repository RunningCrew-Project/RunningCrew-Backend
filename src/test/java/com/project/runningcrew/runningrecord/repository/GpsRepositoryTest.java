package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.runningrecord.entity.Gps;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GpsRepositoryTest {

    @Autowired
    GpsRepository gpsRepository;

    @Autowired
    RunningRecordRepository runningRecordRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("유저로 GPS 삭제 테스트")
    @Test
    public void deleteAllByUserTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .title("personal")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();

        List<Gps> gpsList = List.of(
                new Gps(37.56667, 126.97806, 0, personalRunningRecord),
                new Gps(37.56667, 126.97806, 1, personalRunningRecord),
                new Gps(37.56667, 126.97806, 2, personalRunningRecord),
                new Gps(37.56667, 126.97806, 3, personalRunningRecord),
                new Gps(37.56667, 126.97806, 4, personalRunningRecord)
        );
        runningRecordRepository.save(personalRunningRecord);

        ///when
        gpsRepository.deleteAllByUser(user);

        //then
        RunningRecord runningRecord = runningRecordRepository.findById(personalRunningRecord.getId()).get();
        assertThat(runningRecord.getGpsList()).isEmpty();
    }
    
}