package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.repository.images.RunningRecordImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningRecordImageServiceTest {

    @Mock
    private RunningRecordImageRepository runningRecordImageRepository;

    @InjectMocks
    private RunningRecordImageService runningRecordImageService;

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

}