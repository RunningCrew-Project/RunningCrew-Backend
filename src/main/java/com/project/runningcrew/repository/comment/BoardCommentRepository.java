package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.comment.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
