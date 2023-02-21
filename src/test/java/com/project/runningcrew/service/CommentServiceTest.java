package com.project.runningcrew.service;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.comment.BoardComment;
import com.project.runningcrew.entity.comment.Comment;
import com.project.runningcrew.entity.comment.RunningNoticeComment;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.repository.comment.BoardCommentRepository;
import com.project.runningcrew.repository.comment.CommentRepository;
import com.project.runningcrew.repository.comment.RunningNoticeCommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @InjectMocks CommentService commentService;


    @DisplayName("BoardComment save 테스트")
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

    @DisplayName("RunningNoticeComment save 테스트")
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


    /**
     * Comment 구현체로 BoardComment 사용함
     * @param member
     * @param board
     * @throws Exception
     */
    @DisplayName("Comment 내용 변경 테스트 -정상 통과")
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



    @DisplayName("Comment findAllByMember 테스트")
    @Test
    void findAllByMemberTest(@Mock Member member, @Mock Board board) throws Exception {
        //given
        List<Comment> commentList = new ArrayList<>();
        for (long i = 0L; i < 10; i++) {
            BoardComment boardComment = new BoardComment(i, member, "detail", board);
            commentList.add(boardComment);
        } // save 10 of BoardComment
        when(commentRepository.findAllByMember(member)).thenReturn(commentList);

        //when
        List<Comment> findCommentList = commentService.findAllByMember(member);

        //then
        assertThat(findCommentList.size()).isEqualTo(10);
        assertThat(findCommentList).isEqualTo(commentList);
    }


    @DisplayName("BoardComment findAllByBoard 테스트")
    @Test
    void findAllByBoardTest(@Mock Member member, @Mock Board board) throws Exception {
        //given
        List<BoardComment> boardCommentList = new ArrayList<>();
        for (long i = 0L; i < 10; i++) {
            BoardComment boardComment = new BoardComment(i, member, "detail", board);
            boardCommentList.add(boardComment);
        } // save 10 of BoardComment
        when(boardCommentRepository.findAllByBoard(board)).thenReturn(boardCommentList);

        //when
        List<BoardComment> findBoardCommentList = commentService.findAllByBoard(board);

        //then
        assertThat(findBoardCommentList.size()).isEqualTo(10);
        assertThat(findBoardCommentList).isEqualTo(boardCommentList);
    }


    @DisplayName("RunningNoticeComment findAllByRunningNotice 테스트")
    @Test
    void findAllByRunningNoticeTest(@Mock Member member, @Mock RunningNotice runningNotice) throws Exception {
        //given
        List<RunningNoticeComment> runningNoticeCommentList = new ArrayList<>();
        for (long i = 0L; i < 10; i++) {
            RunningNoticeComment runningNoticeComment = new RunningNoticeComment(i, member, "detail", runningNotice);
            runningNoticeCommentList.add(runningNoticeComment);
        } // save 10 of BoardComment
        when(runningNoticeCommentRepository.findAllByRunningNotice(runningNotice)).thenReturn(runningNoticeCommentList);

        //when
        List<RunningNoticeComment> findRunningNoticeCommentList = commentService.findAllByRunningNotice(runningNotice);

        //then
        assertThat(findRunningNoticeCommentList.size()).isEqualTo(10);
        assertThat(findRunningNoticeCommentList).isEqualTo(runningNoticeCommentList);
    }



}