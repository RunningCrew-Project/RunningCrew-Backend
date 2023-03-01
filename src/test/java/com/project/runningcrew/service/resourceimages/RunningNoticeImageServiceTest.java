package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
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
class RunningNoticeImageServiceTest {

    @Mock
    private RunningNoticeImageRepository runningNoticeImageRepository;

    @InjectMocks
    private RunningNoticeImageService runningNoticeImageService;

    @DisplayName("런닝공지에 포함된 모든 이미지 반환 테스트")
    @Test
    public void findAllByRunningNoticeTest(@Mock RunningNotice runningNotice) {
        //given
        List<RunningNoticeImage> runningNoticeImages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            runningNoticeImages.add(new RunningNoticeImage("image" + i, runningNotice));
        }
        when(runningNoticeImageRepository.findAllByRunningNotice(runningNotice)).thenReturn(runningNoticeImages);

        ///when
        List<RunningNoticeImage> result = runningNoticeImageService.findAllByRunningNotice(runningNotice);

        //then
        assertThat(result.size()).isSameAs(10);
        verify(runningNoticeImageRepository, times(1)).findAllByRunningNotice(runningNotice);
    }

}