package com.project.runningcrew.service;

import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.RunningRecordNotFoundException;
import com.project.runningcrew.repository.runningrecords.RunningRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningRecordServiceTest {

    @Mock
    private RunningRecordRepository runningRecordRepository;

    @InjectMocks
    private RunningRecordService runningRecordService;

    @DisplayName("findById success 테스트")
    @Test
    public void findByIdTest1(@Mock User user) {
        //given
        Long id = 1L;
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .id(id)
                .startDateTime(LocalDateTime.of(2023, 2, 16, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        when(runningRecordRepository.findById(id)).thenReturn(Optional.of(personalRunningRecord));

        ///when
        RunningRecord runningRecord = runningRecordService.findById(id);

        //then
        assertThat(runningRecord).isEqualTo(personalRunningRecord);
    }

    @DisplayName("findById throw exception 테스트")
    @Test
    public void findByIdTest2() {
        //given
        Long id = 1L;
        when(runningRecordRepository.findById(id)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> runningRecordService.findById(id))
                .isInstanceOf(RunningRecordNotFoundException.class);
        verify(runningRecordRepository, times(1)).findById(id);
    }

    @DisplayName("saveRunningRecord 테스트")
    @Test
    public void saveRunningRecordTest(@Mock User user) {
        //given
        Long id = 1L;
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .id(id)
                .startDateTime(LocalDateTime.of(2023, 2, 16, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        when(runningRecordRepository.save(personalRunningRecord)).thenReturn(personalRunningRecord);

        ///when
        Long runningRecordId = runningRecordService.saveRunningRecord(personalRunningRecord);

        //then
        verify(runningRecordRepository, times(1)).save(any());
        assertThat(runningRecordId).isSameAs(id);
    }

    @DisplayName("findByUser 첫 페이지 테스트")
    @Test
    public void findByUserTest1(@Mock User user) {
        //given
        List<RunningRecord> runningRecords = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(LocalDateTime.now())
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            runningRecords.add(personalRunningRecord);
        }
        PageRequest pageRequest = PageRequest.of(0, 7);
        SliceImpl<RunningRecord> runningRecordSlice = new SliceImpl<>(runningRecords, pageRequest, true);
        when(runningRecordRepository.findByUser(user, pageRequest)).thenReturn(runningRecordSlice);

        ///when
        Slice<RunningRecord> result = runningRecordService.findByUser(user, pageRequest);

        //then
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
    }

    @DisplayName("findByUser 중간 페이지 테스트")
    @Test
    public void findByUserTest2(@Mock User user) {
        //given
        List<RunningRecord> runningRecords = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(LocalDateTime.now())
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            runningRecords.add(personalRunningRecord);
        }
        PageRequest pageRequest = PageRequest.of(1, 7);
        SliceImpl<RunningRecord> runningRecordSlice = new SliceImpl<>(runningRecords, pageRequest, true);
        when(runningRecordRepository.findByUser(user, pageRequest)).thenReturn(runningRecordSlice);

        ///when
        Slice<RunningRecord> result = runningRecordService.findByUser(user, pageRequest);

        //then
        assertThat(result.getNumber()).isSameAs(1);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isLast()).isFalse();
    }

    @DisplayName("findByUser 마지막 페이지 테스트")
    @Test
    public void findByUserTest3(@Mock User user) {
        //given
        List<RunningRecord> runningRecords = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(LocalDateTime.now())
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            runningRecords.add(personalRunningRecord);
        }
        PageRequest pageRequest = PageRequest.of(2, 7);
        SliceImpl<RunningRecord> runningRecordSlice = new SliceImpl<>(runningRecords, pageRequest, false);
        when(runningRecordRepository.findByUser(user, pageRequest)).thenReturn(runningRecordSlice);

        ///when
        Slice<RunningRecord> result = runningRecordService.findByUser(user, pageRequest);

        //then
        assertThat(result.getNumber()).isSameAs(2);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(4);
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.hasNext()).isFalse();
        assertThat(result.isLast()).isTrue();
    }

    @DisplayName("findAllByUserAndStartDate 테스트")
    @Test
    public void findAllByUserAndStartDateTest(@Mock User user) {
        //given
        LocalDate date = LocalDate.of(2023, 2, 16);
        List<LocalDateTime> startDates = List.of(
                LocalDateTime.of(2023, 3, 13, 0, 0),
                LocalDateTime.of(2023, 3, 13, 11, 20));

        List<RunningRecord> runningRecords = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(startDates.get(i))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            runningRecords.add(personalRunningRecord);
        }
        when(runningRecordRepository.findAllByUserAndStartDateTimes(user,
                LocalDateTime.of(2023, 2, 16, 0, 0),
                LocalDateTime.of(2023, 2, 17, 0, 0)))
                .thenReturn(runningRecords);

        ///when
        List<RunningRecord> result = runningRecordService.findAllByUserAndStartDate(user, date);

        //then
        assertThat(result.size()).isSameAs(2);
        for (int i = 0; i < 2; i++) {
            assertThat(result.get(i)).isEqualTo(runningRecords.get(i));
        }

        verify(runningRecordRepository, times(1))
                .findAllByUserAndStartDateTimes(any(), any(), any());
    }

}