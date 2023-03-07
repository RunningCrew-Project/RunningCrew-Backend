package com.project.runningcrew.comment.service;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.comment.repository.BoardCommentRepository;
import com.project.runningcrew.comment.repository.CommentRepository;
import com.project.runningcrew.comment.repository.RunningNoticeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final RunningNoticeCommentRepository runningNoticeCommentRepository;




    /**
     * 입력된 Comment 를 저장한다.
     * @param comment
     * @return 저장된 BoardComment 의 id
     */
    public Long saveComment(Comment comment) {
        return commentRepository.save(comment).getId();
    }


    /**
     * 새로운 내용이 기존의 내용과 다르다면 댓글 수정이 가능하다.
     * @param comment
     * @param newDetail
     */
    public void changeComment(Comment comment, String newDetail) {
        if(!comment.getDetail().equals(newDetail)) {
            comment.updateDetail(newDetail);
        }
    }










    /**
     * delete
     */



    /**
     * 하나의 Comment 를 삭제한다.
     * @param comment
     */
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }


    /**
     * 입력받은 Board 에 작성된 BoardComment 를 모두 삭제한다.
     * @param board 댓글을 삭제할 Board
     */
    public void deleteCommentAtBoard(Board board) {
        boardCommentRepository.deleteCommentAtBoard(board);
    }


    /**
     * 입력받은 RunningNotice 에 작성된 RunningNoticeComment 를 모두 삭제한다.
     * @param runningNotice 댓글을 삭제할 RunningNotice
     */
    public void deleteCommentAtRunningNotice(RunningNotice runningNotice) {
        runningNoticeCommentRepository.deleteCommentAtRunningNotice(runningNotice);
    }












    /**
     * 입력된 Member 가 작성한 Comment Slice 를 반환한다.
     * @param member
     * @param pageable
     * @return 입력받은 member 가 작성한 Comment Slice
     */
    public Slice<Comment> findAllByMember(Member member, Pageable pageable) {
        return commentRepository.findAllByMember(member, pageable);
    }

    /**
     * 입력된 Board 에 작성된 BoardComment List 를 반환한다.
     * @param board
     * @return 입력받은 Board 에 작성된 BoardComment list
     */
    public List<BoardComment> findAllByBoard(Board board) {
        return boardCommentRepository.findAllByBoard(board);
    }

    /**
     * 입력된 RunningNotice 에 작성된 RunningNoticeComment List 를 반환한다.
     * @param runningNotice
     * @return 입력받은 RunningNotice 에 작성된 RunningNoticeComment list
     */
    public List<RunningNoticeComment> findAllByRunningNotice(RunningNotice runningNotice) {
        return runningNoticeCommentRepository.findAllByRunningNotice(runningNotice);
    }










    /**
     * 하나의 Board 에 작성된 Comment 와
     * 하나의 RunningNotice 에 작성된 Comment 의 갯수를 반환하는 메소드들
     */


    /**
     * 입력된 Board 에 작성된 Comment 의 수를 반환한다.
     * @param board
     * @return 입력받은 Board 에 작성된 Comment 의 수
     */
    public int countCommentAtBoard(Board board) {
        return boardCommentRepository.findAllByBoard(board).size();
    }


    /**
     * 입력된 RunningNotice 에 작성된 Comment 의 수를 반환한다.
     * @param runningNotice
     * @return 입력받은 RunningNotice 에 작성된 Comment 의 수
     */
    public int countCommentAtRunningNotice(RunningNotice runningNotice) {
        return runningNoticeCommentRepository.findAllByRunningNotice(runningNotice).size();
    }









    /**
     * 입력받은 Board 와 RunningNotice 의 idList 값으로
     * commentCountList 값을 반환하는 메소드들
     */


    /**
     * 입력받은 runningNoticeIdList 정보로 commentCountList 를 만들어 반환한다.
     * @param idList
     * @return commentCountList
     */
    public List<Integer> countByRunningNoticeIdList(List<Long> idList) {
        return runningNoticeCommentRepository.countByRunningNoticeId(idList);
    }

    /**
     * 입력받은 boardIdList 정보로 commentCountList 를 만들어 반환한다.
     * @param idList
     * @return commentCountList
     */
    public List<Integer> countByBoardIdList(List<Long> idList) {
        return boardCommentRepository.countByBoardId(idList);
    }





}
