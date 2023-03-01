package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.repository.images.BoardImageRepository;
import com.project.runningcrew.service.resourceimages.BoardImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardImageServiceTest {

    @Mock
    private BoardImageRepository boardImageRepository;

    @InjectMocks
    private BoardImageService boardImageService;

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
        List<BoardImage> result = boardImageService.findAllByBoard(board);

        //then
        assertThat(result.size()).isSameAs(10);
        verify(boardImageRepository, times(1)).findAllByBoard(board);
    }

}