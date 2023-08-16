package com.project.runningcrew.comment.service;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.common.dto.SimpleCommentDto;
import com.project.runningcrew.exception.notFound.CommentNotFoundException;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.comment.repository.BoardCommentRepository;
import com.project.runningcrew.comment.repository.CommentRepository;
import com.project.runningcrew.comment.repository.RunningNoticeCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final RunningNoticeCommentRepository runningNoticeCommentRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    /**
     * 입력된 Comment 를 저장한다.
     * @param comment 댓글 정보
     * @return 저장된 BoardComment 의 id
     */
    public Long saveComment(Comment comment) {
        return commentRepository.save(comment).getId();
    }

    /**
     * 새로운 내용이 기존의 내용과 다르다면 댓글 수정이 가능하다.
     * @param comment 댓글 정보
     * @param newDetail 새로운 내용 정보
     */
    public void changeComment(Comment comment, String newDetail) {
        if(!comment.getDetail().equals(newDetail)) {
            comment.updateDetail(newDetail);
        }
    }

    /**
     * 하나의 Comment 를 삭제한다.
     * @param comment 댓글 정보
     */
    public void deleteComment(Comment comment) {
        comment.updateDeleted(true);
    }

    /**
     * 입력받은 Board 에 작성된 BoardComment 를 모두 삭제한다.
     * @param board 댓글을 삭제할 Board
     */
    public void deleteCommentAtBoard(Board board) {
        boardCommentRepository.deleteCommentAtBoard(board);
    }

    /**
     * 입력된 Board 에 작성된 BoardComment List 를 SimpleCommentDto 형태로 매핑하여 반환한다.
     * @param board
     * @return 입력받은 Board 에 작성된 BoardComment 의 SimpleCommentDto 리스트
     */
    public List<SimpleCommentDto> findAllByBoard(Board board) {
        return boardCommentRepository.findAllByBoard(board);
    }

    /**
     * 입력된 RunningNotice 에 작성된 RunningNoticeComment List 를 SimpleCommentDto 형태로 매핑하여 반환한다.
     * @param runningNotice
     * @return 입력받은 RunningNotice 에 작성된 RunningNoticeComment 의 SimpleCommentDto 리스트
     */
    public List<SimpleCommentDto> findAllByRunningNotice(RunningNotice runningNotice) {
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
     * boardIdList 로 commentCountList 받아오기
     *
     * -사용 : boardCommentRepository.countAllByBoardIds() 에서 댓글 수가 0개인 경우 0이 입력되지 않고 아예 값 할당이 되지않음.
     *        Maps.getOrDefault(boardId, 0L) 등의 방식으로 디폴드 값 할당 요구함.
     *
     */
    public Map<Long, Long> countAllByBoardIds(List<Long> boardIds) {

        log.info("boardIds = {}", boardIds);
        Map<Long, Long> countMap = boardCommentRepository.countAllByBoardIds(boardIds)
                .stream().collect(Collectors.toMap(o -> (Long) o[0], o -> (Long) o[1]));

        log.info("countMap ={}", countMap);
        return boardIds.stream()
                .collect(Collectors.toMap(o -> o, o -> Objects.requireNonNullElse(countMap.get(o), 0L)));
    }

    /**
     * runningNoticeIdList 로 commentCountList 받아오기
     *
     * -사용 : runningNoticeCommentRepository.countAllByRunningNoticeIds() 에서 댓글 수가 0개인 경우 0이 입력되지 않고 아예 값 할당이 되지않음.
     *        Maps.getOrDefault(runningNoticeId, 0L) 등의 방식으로 디폴드 값 할당 요구함.
     *
     */
    public Map<Long, Long> countAllByRunningNoticeIds(List<Long> runningNoticeIds) {
        Map<Long, Long> countMap = runningNoticeCommentRepository.countAllByRunningNoticeIds(runningNoticeIds)
                .stream().collect(Collectors.toMap(o -> (Long) o[0], o -> (Long) o[1]));

        return runningNoticeIds.stream()
                .collect(Collectors.toMap(o -> o, o -> Objects.requireNonNullElse(countMap.get(o), 0L)));

    }

}
