package com.project.runningcrew.comment.service;

import com.project.runningcrew.comment.service.CommentService;
import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.comment.repository.BoardCommentRepository;
import com.project.runningcrew.comment.repository.CommentRepository;
import com.project.runningcrew.comment.repository.RunningNoticeCommentRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {


    @Mock CommentRepository commentRepository;
    @Mock BoardCommentRepository boardCommentRepository;
    @Mock RunningNoticeCommentRepository runningNoticeCommentRepository;

    @InjectMocks
    CommentService commentService;


    @DisplayName("게시글에 작성된 댓글 저장 테스트")
    @Test
    void saveCommentTest1(@Mock Member member, @Mock Board board) throws Exception {
        //given
        Long id = 1L;
        BoardComment boardComment = new BoardComment(id, member, "detail", board);
        when(commentRepository.save(boardComment)).thenReturn(boardComment);

        //when
        Long BoardCommentId = commentService.saveComment(boardComment);

        //then
        verify(commentRepository, times(1)).save(any());
        assertThat(BoardCommentId).isSameAs(id);
    }

    @DisplayName("런닝 공지에 작성된 댓글 저장 테스트")
    @Test
    void saveCommentTest2(@Mock Member member, @Mock RunningNotice runningNotice) throws Exception {
        //given
        Long id = 1L;
        RunningNoticeComment runningNoticeComment = new RunningNoticeComment(id, member, "detail", runningNotice);
        when(commentRepository.save(runningNoticeComment)).thenReturn(runningNoticeComment);

        //when
        Long RunningNoticeCommentId = commentService.saveComment(runningNoticeComment);

        //then
        verify(commentRepository, times(1)).save(any());
        assertThat(RunningNoticeCommentId).isSameAs(id);
    }


    @DisplayName("댓글 내용 변경 테스트")
    @Test
    void changeCommentTest1(@Mock Member member, @Mock Board board) throws Exception {
        //given
        Long id = 1L;
        BoardComment boardComment = new BoardComment(id, member, "old_detail", board);

        when(commentRepository.save(boardComment)).thenReturn(boardComment);
        Long savedComment = commentService.saveComment(boardComment);

        //when
        commentService.changeComment(boardComment, "new_detail");

        //then
        assertThat(boardComment.getDetail()).isEqualTo("new_detail");
    }


    @DisplayName("특정 멤버가 작성한 댓글 모두 가져오기 테스트")
    @Test
    void findAllByMemberTest(@Mock Member member, @Mock Board board) throws Exception {
        //given
        List<Comment> commentList = new ArrayList<>();
        for (long i = 0L; i < 10; i++) {
            BoardComment boardComment = new BoardComment(i, member, "detail", board);
            commentList.add(boardComment);
        } // save 10 of BoardComment
        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<Comment> commentSlice = new SliceImpl<>(commentList, pageRequest, false);
        when(commentRepository.findAllByMember(member, pageRequest)).thenReturn(commentSlice);

        //when
        Slice<Comment> findCommentSlice = commentRepository.findAllByMember(member, pageRequest);

        //then
        assertThat(findCommentSlice.hasNext()).isFalse();
        assertThat(findCommentSlice.isFirst()).isTrue();
        assertThat(findCommentSlice.getSize()).isEqualTo(10);
        assertThat(findCommentSlice.getNumberOfElements()).isEqualTo(10);
        assertThat(findCommentSlice.getNumber()).isEqualTo(0);
    }

//
//    @DisplayName("게시글에 작성된 댓글 모두 가져오기 테스트")
//    @Test
//    void findAllByBoardTest(@Mock Member member, @Mock Board board) throws Exception {
//        //given
//        List<BoardComment> boardCommentList = new ArrayList<>();
//        for (long i = 0L; i < 10; i++) {
//            BoardComment boardComment = new BoardComment(i, member, "detail", board);
//            boardCommentList.add(boardComment);
//        } // save 10 of BoardComment
//        when(boardCommentRepository.findAllByBoard(board)).thenReturn(boardCommentList);
//
//        //when
//        List<BoardComment> findBoardCommentList = commentService.findAllByBoard(board);
//
//        //then
//        assertThat(findBoardCommentList.size()).isEqualTo(10);
//        assertThat(findBoardCommentList).isEqualTo(boardCommentList);
//    }


//    @DisplayName("런닝 공지에 작성된 댓글 모두 가져오기 테스트")
//    @Test
//    void findAllByRunningNoticeTest(@Mock Member member, @Mock RunningNotice runningNotice) throws Exception {
//        //given
//        List<RunningNoticeComment> runningNoticeCommentList = new ArrayList<>();
//        for (long i = 0L; i < 10; i++) {
//            RunningNoticeComment runningNoticeComment = new RunningNoticeComment(i, member, "detail", runningNotice);
//            runningNoticeCommentList.add(runningNoticeComment);
//        } // save 10 of BoardComment
//        when(runningNoticeCommentRepository.findAllByRunningNotice(runningNotice)).thenReturn(runningNoticeCommentList);
//
//        //when
//        List<RunningNoticeComment> findRunningNoticeCommentList = commentService.findAllByRunningNotice(runningNotice);
//
//        //then
//        assertThat(findRunningNoticeCommentList.size()).isEqualTo(10);
//        assertThat(findRunningNoticeCommentList).isEqualTo(runningNoticeCommentList);
//    }


//
//    @DisplayName("게시글에 작성된 댓글 수 가져오기 테스트")
//    @Test
//    void countCommentAtBoardTest(@Mock Member member, @Mock Board board) throws Exception {
//        //given
//        List<BoardComment> boardCommentList = new ArrayList<>();
//        for (long i = 0L; i < 10; i++) {
//            BoardComment boardComment = new BoardComment(i, member, "detail", board);
//            boardCommentList.add(boardComment);
//        } // save 10 of BoardComment
//        when(boardCommentRepository.findAllByBoard(board)).thenReturn(boardCommentList);
//
//        //when
//        int count = commentService.countCommentAtBoard(board);
//
//        //then
//        assertThat(count).isEqualTo(10);
//    }
//
//    @DisplayName("런닝 공지에 작성된 댓글 수 가져오기 테스트")
//    @Test
//    void countCommentAtRunningNoticeTest(@Mock Member member, @Mock RunningNotice runningNotice) throws Exception {
//        //given
//        List<RunningNoticeComment> runningNoticeCommentList = new ArrayList<>();
//        for (long i = 0L; i < 10; i++) {
//            RunningNoticeComment runningNoticeComment = new RunningNoticeComment(i, member, "detail", runningNotice);
//            runningNoticeCommentList.add(runningNoticeComment);
//        } // save 10 of BoardComment
//        when(runningNoticeCommentRepository.findAllByRunningNotice(runningNotice)).thenReturn(runningNoticeCommentList);
//
//        //when
//        int count = commentService.countCommentAtRunningNotice(runningNotice);
//
//        //then
//        assertThat(count).isEqualTo(10);
//    }

}