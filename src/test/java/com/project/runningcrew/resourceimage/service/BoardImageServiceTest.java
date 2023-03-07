package com.project.runningcrew.resourceimage.service;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
import com.project.runningcrew.resourceimage.service.BoardImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @DisplayName("boardId 리스트를 받아 boardId 와 BoardImage 의 Map 반환 테스트")
    @Test
    public void findFirstImagesTest(@Mock Member member) {
        //given
        List<Long> boardIds = List.of(1L, 2L, 3L);
        List<BoardImage> boardImages = new ArrayList<>();
        FreeBoard freeBoard1 = new FreeBoard(1L, member, "title", "detail");
        for (int i = 0; i < 3; i++) {
            BoardImage boardImage = new BoardImage("image" + i, freeBoard1);
            boardImages.add(boardImage);
        }
        FreeBoard freeBoard3 = new FreeBoard(3L, member, "title", "detail");
        for (int i = 0; i < 2; i++) {
            BoardImage boardImage = new BoardImage("image" + i, freeBoard3);
            boardImages.add(boardImage);
        }
        when(boardImageRepository.findImagesByBoardIds(boardIds)).thenReturn(boardImages);

        ///when
        Map<Long, BoardImage> firstImages = boardImageService.findFirstImages(boardIds);

        //then
        assertThat(firstImages.get(1L).getBoard().getId()).isSameAs(1L);
        assertThat(firstImages.get(2L).getFileName()).isEqualTo("defaultImageUrl");
        assertThat(firstImages.get(3L).getBoard().getId()).isSameAs(3L);
        verify(boardImageRepository, times(1)).findImagesByBoardIds(boardIds);
    }

}