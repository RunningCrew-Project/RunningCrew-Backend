package com.project.runningcrew.runningrecord.service;

import com.project.runningcrew.resourceimage.entity.RunningRecordImage;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.runningrecord.service.RunningRecordService;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.notFound.RunningRecordNotFoundException;
import com.project.runningcrew.resourceimage.repository.RunningRecordImageRepository;
import com.project.runningcrew.runningrecord.repository.RunningRecordRepository;
import com.project.runningcrew.image.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningRecordServiceTest {

    @Mock
    private RunningRecordRepository runningRecordRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private RunningRecordImageRepository runningRecordImageRepository;

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
        verify(runningRecordRepository, times(1)).findById(id);
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
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            multipartFiles.add(new MockMultipartFile("image", "".getBytes()));
        }
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
        when(imageService.uploadImage(any(), any())).thenReturn("crewImgURl");
        when(runningRecordImageRepository.save(any())).thenReturn(new RunningRecordImage("crewImgURl", personalRunningRecord));

        ///when
        Long runningRecordId = runningRecordService.saveRunningRecord(personalRunningRecord, multipartFiles);

        //then
        verify(runningRecordRepository, times(1)).save(any());
        verify(imageService, times(multipartFiles.size())).uploadImage(any(), any());
        verify(runningRecordImageRepository, times(multipartFiles.size())).save(any());
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
        verify(runningRecordRepository, times(1)).findByUser(user, pageRequest);
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
        verify(runningRecordRepository, times(1)).findByUser(user, pageRequest);
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
        verify(runningRecordRepository, times(1)).findByUser(user, pageRequest);
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

    @DisplayName("유저의 모든 RunningRecord 삭제")
    @Test
    public void deleteAllByUserTest(@Mock User user) {
        //given
        doNothing().when(runningRecordRepository).deleteAllByUser(user);

        ///when
        runningRecordService.deleteAllByUser(user);

        //then
        verify(runningRecordRepository,times(1)).deleteAllByUser(user);
    }

    @DisplayName("특정 년도 특정 달에 시행한 유저의 모든 RunningRecord 의 런닝 거리의 합 테스트")
    @Test
    public void getSumOfRunningDistanceOfMonthTest(@Mock User user) {
        //given
        Double sum = Double.valueOf(5.1);
        LocalDateTime start = LocalDateTime.of(2023, 2, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 3, 1, 0, 0);
        when(runningRecordRepository.getSumOfRunningDistanceByUserAndStartDateTimes(user, start, end))
                .thenReturn(sum);

        ///when
        Double result = runningRecordService.getSumOfRunningDistanceOfMonth(user, 2023, 2);

        //then
        assertThat(result).isEqualTo(sum);
        verify(runningRecordRepository, times(1))
                .getSumOfRunningDistanceByUserAndStartDateTimes(user, start, end);
    }

    @DisplayName("특정 년도 특정 달에 시행한 유저의 모든 RunningRecord 의 런닝 시간의 합 테스트")
    @Test
    public void getSumOfRunningTimeOfMonthTest(@Mock User user) {
        Integer sum = Integer.valueOf(6500);
        LocalDateTime start = LocalDateTime.of(2023, 2, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 3, 1, 0, 0);
        when(runningRecordRepository.getSumOfRunningTimeByUserAndStartDateTimes(user, start, end))
                .thenReturn(sum);

        ///when
        Integer result = runningRecordService.getSumOfRunningTimeOfMonth(user, 2023, 2);

        //then
        assertThat(result).isEqualTo(sum);
        verify(runningRecordRepository, times(1))
                .getSumOfRunningTimeByUserAndStartDateTimes(user, start, end);
    }

}