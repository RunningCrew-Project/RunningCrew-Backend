package com.project.runningcrew.board.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.exception.notFound.BoardNotFoundException;
import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.board.repository.FreeBoardRepository;
import com.project.runningcrew.board.repository.NoticeBoardRepository;
import com.project.runningcrew.board.repository.ReviewBoardRepository;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
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


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock BoardRepository boardRepository;
    @Mock FreeBoardRepository freeBoardRepository;
    @Mock NoticeBoardRepository noticeBoardRepository;
    @Mock ReviewBoardRepository reviewBoardRepository;
    @Mock BoardImageRepository boardImageRepository;
    @Mock ImageService imageService;

    @InjectMocks
    BoardService boardService;

    @InjectMocks
    FreeBoardService freeBoardService;

    @InjectMocks
    NoticeBoardService noticeBoardService;

    @InjectMocks
    ReviewBoardService reviewBoardService;



    @DisplayName("아이디로 게시글 가져오기 테스트 - 성공")
    @Test
    void findByIdTest(@Mock Member member) throws Exception {
        //given
        Long boardId = 1L;
        Board board = new FreeBoard(member, "content", "detail");
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        //when
        Board findBoard = boardService.findById(boardId);

        //then
        assertThat(findBoard).isEqualTo(board);
    }

    @DisplayName(" 게시글 가져오기 테스트 - 예외 발생")
    @Test
    void findByIdTest2(@Mock Member member) throws Exception {
        //given
        Long boardId = 1L;
        Board board = new FreeBoard(member, "content", "detail");
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> boardService.findById(boardId))
                .isInstanceOf(BoardNotFoundException.class);
    }

    @DisplayName("게시글 저장하기 테스트")
    @Test
    void saveBoardTest(@Mock Member member) throws Exception {

    }


    @DisplayName("게시글 업데이트 테스트")
    @Test
    void updateBoardTest(@Mock Member member) throws Exception {
    }

    @DisplayName("게시글 삭제하기 테스트")
    @Test
    void deleteTest() throws Exception {

    }

    @DisplayName("특정 멤버가 작성한 게시글 가져오기 - 페이징 적용")
    @Test
    void findBoardByMemberTest(@Mock Member member) throws Exception {

    }



    @DisplayName("특정 크루의 공지게시판 목록 가져오기 - 페이징 적용")
    @Test
    void findNoticeBoardByCrewTest(@Mock Member member, @Mock Crew crew) throws Exception {

    }

    @DisplayName("특정 크루의 리뷰게시판 목록 가져오기 - 페이징 적용")
    @Test
    void findReviewBoardByCrewTest() throws Exception {

    }


}
