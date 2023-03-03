package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.exception.notFound.BoardNotFoundException;
import com.project.runningcrew.repository.boards.BoardRepository;
import com.project.runningcrew.repository.boards.FreeBoardRepository;
import com.project.runningcrew.repository.boards.NoticeBoardRepository;
import com.project.runningcrew.repository.boards.ReviewBoardRepository;
import com.project.runningcrew.repository.images.BoardImageRepository;
import com.project.runningcrew.service.boards.BoardService;
import com.project.runningcrew.service.boards.FreeBoardService;
import com.project.runningcrew.service.boards.NoticeBoardService;
import com.project.runningcrew.service.boards.ReviewBoardService;
import com.project.runningcrew.service.images.ImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
        //given
        Long boardId = 1L;
        String boardImgUrl = "boardImgUrl";
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            multipartFiles.add(new MockMultipartFile("image", "".getBytes()));
        }

        Board board = new FreeBoard(boardId, member, "content", "detail");
        when(boardRepository.save(board)).thenReturn(board);
        when(imageService.uploadImage(any(), any())).thenReturn(boardImgUrl);
        when(boardImageRepository.save(any())).thenReturn(new BoardImage(boardImgUrl, board));

        //when
        Long savedId = boardService.saveBoard(board, multipartFiles);

        //then
        assertThat(savedId).isEqualTo(boardId);
    }


    @DisplayName("게시글 업데이트 테스트")
    @Test
    void updateBoardTest(@Mock Member member) throws Exception {
        //given
        String boardImgUrl = "boardImgUrl";

        Board originBoard = new FreeBoard(member, "content", "detail");
        FreeBoard newBoard = new FreeBoard(member, "new_content", "new_detail");

        List<MultipartFile> addFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            addFiles.add(new MockMultipartFile("image", "".getBytes()));
        }

        List<BoardImage> deleteFiles = List.of(
                new BoardImage("image1", originBoard),
                new BoardImage("image1", originBoard),
                new BoardImage("image1", originBoard)
        );

        //when
        boardService.updateBoard(originBoard, newBoard, addFiles, deleteFiles);

        //then
        assertThat(originBoard.getTitle()).isEqualTo(newBoard.getTitle());
        assertThat(originBoard.getDetail()).isEqualTo(newBoard.getDetail());
        verify(imageService, times(addFiles.size())).uploadImage(any(), any());
        verify(boardImageRepository, times(addFiles.size())).save(any());
        verify(imageService, times(deleteFiles.size())).deleteImage(any());
        verify(boardImageRepository, times(deleteFiles.size())).delete(any());
    }

    @DisplayName("게시글 삭제하기 테스트")
    @Test
    void deleteTest() throws Exception {
        /**
         * 추후 구현
         */
    }

    @DisplayName("특정 멤버가 작성한 게시글 가져오기 - 페이징 적용")
    @Test
    void findBoardByMemberTest(@Mock Member member) throws Exception {
        //given
        List<Board> boardList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            boardList.add(new FreeBoard(member, "title" + i, "content" + i));
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<Board> boardSlice = new SliceImpl<>(boardList, pageRequest, false);
        when(boardService.findBoardByMember(member, pageRequest)).thenReturn(boardSlice);

        //when
        Slice<Board> findSlice = boardService.findBoardByMember(member, pageRequest);
        List<Board> findList = findSlice.getContent();

        //then
        assertThat(findList.size()).isEqualTo(7);
        assertThat(findSlice.getNumberOfElements()).isEqualTo(7);
        assertThat(findSlice.getSize()).isEqualTo(10);
        assertThat(findSlice.isFirst()).isTrue();
        assertThat(findSlice.getNumber()).isEqualTo(0);
        assertThat(findSlice.hasNext()).isFalse();
    }

    @DisplayName("특정 키워드를 제목 or 내용에 포함하는 게시글 가져오기 - 페이징 적용")
    @Test
    void findBoardByCrewAndKeyWordTest(@Mock Member member, @Mock Crew crew) throws Exception {
        //given
        String keyword = "key";
        List<Board> boardList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            boardList.add(new FreeBoard(member, "title" + i, "content" + i));
        }
        for (int i = 0; i < 3; i++) {
            boardList.add(new FreeBoard(member, "ti_key_tle" + i, "content" + i));
        }
        for (int i = 0; i < 3; i++) {
            boardList.add(new FreeBoard(member, "title" + i, "cont_key_ent" + i));
        }

        List<Board> realList = new ArrayList<>();
        for (Board board : boardList) {
            if(board.getDetail().contains(keyword) || board.getTitle().contains(keyword)) {
                realList.add(board);
            }
        }

        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<Board> boardSlice = new SliceImpl<>(realList, pageRequest, false);
        when(boardService.findBoardByCrewAndKeyWord(crew, keyword)).thenReturn(boardSlice);

        //when
        Slice<Board> findSlice = boardService.findBoardByCrewAndKeyWord(crew, keyword);
        List<Board> findList = findSlice.getContent();

        //then
        assertThat(findList.size()).isEqualTo(6);
        assertThat(findSlice.getNumberOfElements()).isEqualTo(6);
        assertThat(findSlice.getSize()).isEqualTo(10);
        assertThat(findSlice.hasNext()).isFalse();
        assertThat(findSlice.isFirst()).isTrue();
    }

    @DisplayName("특정 크루의 자유게시판 목록 가져오기 - 페이징 적용")
    @Test
    void findFreeBoardByCrewTest(@Mock Member member, @Mock Crew crew) throws Exception {
        //given
        List<FreeBoard> boardList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            boardList.add(new FreeBoard(member, "title" + i, "content" + i));
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<FreeBoard> boardSlice = new SliceImpl<>(boardList, pageRequest, false);
        when(freeBoardRepository.findFreeBoardByCrew(crew, pageRequest)).thenReturn(boardSlice);

        //when
        Slice<FreeBoard> findSlice = freeBoardService.findFreeBoardByCrew(crew, pageRequest);
        List<FreeBoard> findList = findSlice.getContent();

        //then
        assertThat(findList.size()).isEqualTo(9);
        assertThat(findSlice.getNumberOfElements()).isEqualTo(9);
        assertThat(findSlice.getSize()).isEqualTo(10);
        assertThat(findSlice.isFirst()).isTrue();
        assertThat(findSlice.hasNext()).isFalse();
    }

    @DisplayName("특정 크루의 공지게시판 목록 가져오기 - 페이징 적용")
    @Test
    void findNoticeBoardByCrewTest(@Mock Member member, @Mock Crew crew) throws Exception {
        //given
        List<NoticeBoard> boardList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            boardList.add(new NoticeBoard(member, "title" + i, "content" + i));
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<NoticeBoard> boardSlice = new SliceImpl<>(boardList, pageRequest, false);
        when(noticeBoardRepository.findNoticeBoardByCrew(crew, pageRequest)).thenReturn(boardSlice);

        //when
        Slice<NoticeBoard> findSlice = noticeBoardService.findNoticeBoardByCrew(crew, pageRequest);
        List<NoticeBoard> findList = findSlice.getContent();

        //then
        assertThat(findList.size()).isEqualTo(9);
        assertThat(findSlice.getNumberOfElements()).isEqualTo(9);
        assertThat(findSlice.getSize()).isEqualTo(10);
        assertThat(findSlice.isFirst()).isTrue();
        assertThat(findSlice.hasNext()).isFalse();
    }

    @DisplayName("특정 크루의 리뷰게시판 목록 가져오기 - 페이징 적용")
    @Test
    void findReviewBoardByCrewTest() throws Exception {
        //given

        //when

        //then

    }


}
