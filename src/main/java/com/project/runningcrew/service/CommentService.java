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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
     * 입력된 Comment 를 삭제한다.
     * @param comment
     */
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    /**
     * 입력된 Member 가 작성한 Comment List 를 반환한다.
     * @param member
     * @return 입력받은 member 가 작성한 Comment list
     */
    public List<Comment> findAllByMember(Member member) {
        return commentRepository.findAllByMember(member);
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
     * Board 리스트를 받아 commentCount 리스트를 반환한다.
     * @param boardList 입력받은 Board 의 List
     * @return commentCountList
     */
    public List<Integer> commentCountListByBoardList(List<Board> boardList) {
        List<Integer> commentCountList = new ArrayList<>();
        for (Board board : boardList) {
            int element = boardCommentRepository.findAllByBoard(board).size();
            commentCountList.add(element);
        }
        return commentCountList;
    }

    /**
     * RunningNotice 리스트를 받아 commentCount 리스트를 반환한다.
     * @param runningNoticeList 입력받은 RunningNotice 의 List
     * @return commentCountList
     */
    public List<Integer> commentCountListByRunningNoticeList(List<RunningNotice> runningNoticeList) {
        List<Integer> commentCountList = new ArrayList<>();
        for (RunningNotice runningNotice : runningNoticeList) {
            int element = runningNoticeCommentRepository.findAllByRunningNotice(runningNotice).size();
            commentCountList.add(element);
        }
        return commentCountList;
    }


}
