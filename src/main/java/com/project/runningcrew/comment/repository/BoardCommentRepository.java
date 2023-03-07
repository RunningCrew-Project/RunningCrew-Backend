package com.project.runningcrew.comment.repository;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findAllByBoard(Board board);

    /**
     * Board 의 id 정보를 리스트로 입력받아 각 Board 의 댓글 갯수 정보를 반환한다.
     * @param boardId Board Id 리스트 정보
     * @return  각 Board 의 댓글 수 리스트
     */
    @Query("select count(bc) from BoardComment bc where bc.board.id in (:boardId) group by bc.board.id")
    List<Integer> countByBoardId(@Param("boardId") List<Long> boardId);


    /**
     * 입력받은 Board 에 작성된 댓글을 모두 삭제한다.
     * @param board 댓글을 삭제할 board
     */
    @Modifying
    @Query("delete from BoardComment bc where bc.board = :board")
    void deleteCommentAtBoard(@Param("board") Board board);




}
