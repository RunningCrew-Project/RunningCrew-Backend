package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.Gps;
import com.project.runningcrew.entity.areas.DongArea;
import com.project.runningcrew.entity.areas.GuArea;
import com.project.runningcrew.entity.areas.SidoArea;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningrecords.CrewRunningRecord;
import com.project.runningcrew.repository.TestEntityFactory;
import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RunningRecordRepositoryTest {

    @Autowired
    RunningRecordRepository runningRecordRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("PersonalRunningRecord save 테스트")
    @Test
    public void saveTest_PersonalRunningRecord() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
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

        ///when
        PersonalRunningRecord savedPersonalRunningRecord = runningRecordRepository.save(personalRunningRecord);

        //then
        List<Gps> savedGpsList = savedPersonalRunningRecord.getGpsList();
        assertThat(savedPersonalRunningRecord).isEqualTo(personalRunningRecord);
        for (int i = 0; i < gpsList.size(); i++) {
            assertThat(savedGpsList.get(i)).isEqualTo(gpsList.get(i));
        }
    }

    @DisplayName("PersonalRunningRecord findById 테스트")
    @Test
    public void findById_PersonalRunningRecord() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        runningRecordRepository.save(personalRunningRecord);

        ///when
        Optional<RunningRecord> optPersonalRunningRecord = runningRecordRepository
                .findById(personalRunningRecord.getId());

        //then
        assertThat(optPersonalRunningRecord).isNotEmpty();
        assertThat(optPersonalRunningRecord).hasValue(personalRunningRecord);
    }

    @DisplayName("PersonalRunningRecord delete 테스트")
    @Test
    public void delete_PersonalRunningRecord() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        runningRecordRepository.save(personalRunningRecord);

        ///when
        runningRecordRepository.delete(personalRunningRecord);

        //then
        Optional<RunningRecord> optPersonalRunningRecord = runningRecordRepository
                .findById(personalRunningRecord.getId());
        assertThat(optPersonalRunningRecord).isEmpty();
    }

    @DisplayName("CrewRunningRecord save 테스트")
    @Test
    public void saveTest_CrewRunningRecord() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);

        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .crew(crew)
                .runningNotice(runningNotice)
                .build();

        List<Gps> gpsList = List.of(
                new Gps(37.56667, 126.97806, 0, crewRunningRecord),
                new Gps(37.56667, 126.97806, 1, crewRunningRecord),
                new Gps(37.56667, 126.97806, 2, crewRunningRecord),
                new Gps(37.56667, 126.97806, 3, crewRunningRecord),
                new Gps(37.56667, 126.97806, 4, crewRunningRecord)
        );

        ///when
        CrewRunningRecord savedCrewRunningRecord = runningRecordRepository.save(crewRunningRecord);

        //then
        List<Gps> savedGpsList = savedCrewRunningRecord.getGpsList();
        assertThat(savedCrewRunningRecord).isEqualTo(crewRunningRecord);
        for (int i = 0; i < gpsList.size(); i++) {
            assertThat(savedGpsList.get(i)).isEqualTo(gpsList.get(i));
        }
    }

    @DisplayName("CrewRunningRecord findById 테스트")
    @Test
    public void findById_CrewRunningRecord() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);

        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .crew(crew)
                .runningNotice(runningNotice)
                .build();
        runningRecordRepository.save(crewRunningRecord);

        ///when
        Optional<RunningRecord> optCrewRunningRecord = runningRecordRepository.findById(crewRunningRecord.getId());

        //then
        assertThat(optCrewRunningRecord).isNotEmpty();
        assertThat(optCrewRunningRecord).hasValue(crewRunningRecord);
    }

    @DisplayName("CrewRunningRecord delete 테스트")
    @Test
    public void delete_CrewRunningRecord() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);
        RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, 0);

        CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .crew(crew)
                .runningNotice(runningNotice)
                .build();
        runningRecordRepository.save(crewRunningRecord);

        ///when
        runningRecordRepository.delete(crewRunningRecord);

        //then
        Optional<RunningRecord> optCrewRunningRecord = runningRecordRepository.findById(crewRunningRecord.getId());
        assertThat(optCrewRunningRecord).isEmpty();
    }

    @DisplayName("첫 페이지의 findByUser 테스트")
    @Test
    public void findByUser_firstPage() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);

        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, i);

            CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                    .startDateTime(LocalDateTime.of(2023, 3, 1 + i, 15, 0))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .crew(crew)
                    .runningNotice(runningNotice)
                    .build();
            runningRecordRepository.save(crewRunningRecord);
        }

        for (int i = 10; i < 20; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(LocalDateTime.of(2023, 3, 10 + i, 15, 0))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            runningRecordRepository.save(personalRunningRecord);
        }
        PageRequest pageRequest = PageRequest.of(0, 7, Sort.by("createdDate").descending());

        ///when
        Slice<RunningRecord> runningRecords = runningRecordRepository.findByUser(user, pageRequest);

        //then
        assertThat(runningRecords.getNumber()).isSameAs(0);
        assertThat(runningRecords.getSize()).isSameAs(7);
        assertThat(runningRecords.getNumberOfElements()).isSameAs(7);
        assertThat(runningRecords.hasPrevious()).isFalse();
        assertThat(runningRecords.hasNext()).isTrue();
    }

    @DisplayName("마지막 페이지의 findByUser 테스트")
    @Test
    public void findByUser_lastPage() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        Crew crew = testEntityFactory.getCrew(dongArea, 0);
        Member member = testEntityFactory.getMember(user, crew);

        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = testEntityFactory.getRunningNotice(member, i);

            CrewRunningRecord crewRunningRecord = CrewRunningRecord.builder()
                    .startDateTime(LocalDateTime.of(2023, 3, 1 + i, 15, 0))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .crew(crew)
                    .runningNotice(runningNotice)
                    .build();
            runningRecordRepository.save(crewRunningRecord);
        }

        for (int i = 10; i < 20; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(LocalDateTime.of(2023, 3, 10 + i, 15, 0))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            runningRecordRepository.save(personalRunningRecord);
        }
        PageRequest pageRequest = PageRequest.of(2, 7, Sort.by("createdDate").descending());

        ///when
        Slice<RunningRecord> runningRecords = runningRecordRepository.findByUser(user, pageRequest);

        //then
        assertThat(runningRecords.getNumber()).isSameAs(2);
        assertThat(runningRecords.getSize()).isSameAs(7);
        assertThat(runningRecords.getNumberOfElements()).isSameAs(6);
        assertThat(runningRecords.hasPrevious()).isTrue();
        assertThat(runningRecords.hasNext()).isFalse();
    }

    @DisplayName("findAllByUserAndStartDateTimes 테스트")
    @Test
    public void findAllByByStartDateTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        List<LocalDateTime> startDates = List.of(
                LocalDateTime.of(2023, 3, 13, 0, 0),
                LocalDateTime.of(2023, 3, 13, 11, 20),
                LocalDateTime.of(2023, 3, 14, 0, 0));
        LocalDateTime today = LocalDateTime.of(2023, 3, 13, 0, 0);
        LocalDateTime tomorrow = today.plusDays(1);

        for (int i = 0; i < 3; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(startDates.get(i))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            runningRecordRepository.save(personalRunningRecord);
        }

        ///when
        List<RunningRecord> personalRunningRecords = runningRecordRepository
                .findAllByUserAndStartDateTimes(user, today, tomorrow);

        //then
        assertThat(personalRunningRecords.size()).isSameAs(2);
    }

}