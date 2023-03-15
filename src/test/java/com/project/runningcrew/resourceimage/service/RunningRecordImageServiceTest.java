package com.project.runningcrew.resourceimage.service;

import com.project.runningcrew.exception.notFound.ImageNotFoundException;
import com.project.runningcrew.resourceimage.entity.RunningRecordImage;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.resourceimage.repository.RunningRecordImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningRecordImageServiceTest {

    @Mock
    private RunningRecordImageRepository runningRecordImageRepository;

    @InjectMocks
    private RunningRecordImageService runningRecordImageService;

    @DisplayName("id 로 RunningRecordImage 반환 성공 테스트")
    @Test
    public void findByIdTest1(@Mock RunningRecord runningRecord) {
        //given
        Long runningRecordImageId = 1L;
        RunningRecordImage runningRecordImage = new RunningRecordImage("image", runningRecord);
        when(runningRecordImageRepository.findById(runningRecordImageId)).thenReturn(Optional.of(runningRecordImage));

        ///when
        RunningRecordImage findRunningRecordImage = runningRecordImageService.findById(runningRecordImageId);

        //then
        assertThat(findRunningRecordImage).isEqualTo(runningRecordImage);
        verify(runningRecordImageRepository, times(1)).findById(runningRecordImageId);
    }

    @DisplayName("id 로 RunningRecordImage 반환 예외 테스트")
    @Test
    public void findByIdTest2() {
        //given
        Long runningRecordImageId = 1L;
        when(runningRecordImageRepository.findById(runningRecordImageId)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> runningRecordImageService.findById(runningRecordImageId))
                .isInstanceOf(ImageNotFoundException.class);
    }

    @DisplayName("런닝기록에 포함된 모든 이미지 반환 테스트")
    @Test
    public void findAllByRunningRecordTest(@Mock RunningRecord runningRecord) {
        //given
        List<RunningRecordImage> runningRecordImages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            runningRecordImages.add(new RunningRecordImage("image" + i, runningRecord));
        }
        when(runningRecordImageRepository.findAllByRunningRecord(runningRecord)).thenReturn(runningRecordImages);

        ///when
        List<RunningRecordImage> result = runningRecordImageService.findAllByRunningRecord(runningRecord);

        //then
        assertThat(result.size()).isSameAs(10);
        verify(runningRecordImageRepository, times(1)).findAllByRunningRecord(runningRecord);
    }

    @DisplayName("runningRecordId 리스트를 받아 runningRecordId 와 RunningRecordImage 의 Map 반환 테스트")
    @Test
    public void findFirstImagesTest(@Mock User user) {
        //given
        List<Long> runningRecordIds = List.of(1L, 2L, 3L);
        List<RunningRecordImage> runningRecordImages = new ArrayList<>();
        PersonalRunningRecord runningRecord1 = PersonalRunningRecord.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(2023, 2, 16, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        for (int i = 0; i < 3; i++) {
            RunningRecordImage runningRecordImage = new RunningRecordImage("image" + i, runningRecord1);
            runningRecordImages.add(runningRecordImage);
        }
        PersonalRunningRecord runningRecord3 = PersonalRunningRecord.builder()
                .id(3L)
                .startDateTime(LocalDateTime.of(2023, 2, 16, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        for (int i = 0; i < 2; i++) {
            RunningRecordImage runningRecordImage = new RunningRecordImage("image" + i, runningRecord3);
            runningRecordImages.add(runningRecordImage);
        }
        when(runningRecordImageRepository.findImagesByRunningRecordIds(runningRecordIds))
                .thenReturn(runningRecordImages);

        ///when
        Map<Long, RunningRecordImage> firstImages = runningRecordImageService.findFirstImages(runningRecordIds);

        //then
        assertThat(firstImages.get(1L).getRunningRecord().getId()).isSameAs(1L);
        assertThat(firstImages.get(2L).getFileName()).isEqualTo("defaultImageUrl");
        assertThat(firstImages.get(3L).getRunningRecord().getId()).isSameAs(3L);
        verify(runningRecordImageRepository, times(1)).findImagesByRunningRecordIds(runningRecordIds);
    }

}