package com.project.runningcrew.service;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.repository.images.BoardImageRepository;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
import com.project.runningcrew.repository.images.RunningRecordImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceImageServiceTest {

    @Mock
    private BoardImageRepository boardImageRepository;

    @Mock
    private RunningNoticeImageRepository runningNoticeImageRepository;

    @Mock
    private RunningRecordImageRepository runningRecordImageRepository;

    @InjectMocks
    private ResourceImageService resourceImageService;

    @DisplayName("게시글에 포함된 모든 이미지 반환 테스트")
    @Test
    public void findAllByBoardTest(@Mock Board board) {
        //given
        List<BoardImage> boardImages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            boardImages.add(new BoardImage("image" + i, board));
        }
        when(boardImageRepository.findAllByBoard(board)).thenReturn(boardImages);

        ///when
        List<BoardImage> result = resourceImageService.findAllByBoard(board);

        //then
        assertThat(result.size()).isSameAs(10);
        verify(boardImageRepository, times(1)).findAllByBoard(board);
    }

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
        List<RunningNoticeImage> result = resourceImageService.findAllByRunningNotice(runningNotice);

        //then
        assertThat(result.size()).isSameAs(10);
        verify(runningNoticeImageRepository, times(1)).findAllByRunningNotice(runningNotice);
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
        List<RunningRecordImage> result = resourceImageService.findAllByRunningRecord(runningRecord);

        //then
        assertThat(result.size()).isSameAs(10);
        verify(runningRecordImageRepository, times(1)).findAllByRunningRecord(runningRecord);
    }

}