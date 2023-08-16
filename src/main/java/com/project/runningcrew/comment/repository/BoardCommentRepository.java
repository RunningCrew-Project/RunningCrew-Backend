package com.project.runningcrew.comment.repository;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.common.dto.SimpleCommentDto;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    /**
     * 입력받은 Board 에 작성된 댓글의 SimpleCommentDto 리스트를 반환한다.
     * @param board 입력받은 board
     * @return 입력받은 Board 의 SimpleCommentDto 리스트
     * SOFT DELETE 적용
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleCommentDto(bc.id, m.id, bc.createdDate, bc.detail, u.nickname, u.imgUrl) " +
            "from BoardComment bc " +
            "inner join Board b on b = bc.board and b.deleted = false " +
            "inner join Member m on m = bc.member and m.deleted = false " +
            "inner join User u on m.user = u and u.deleted = false " +
            "where bc.board = :board")
    List<SimpleCommentDto> findAllByBoard(@Param("board") Board board);

    /**
     * 입력받은 Board 에 작성된 댓글을 모두 삭제한다.
     * @param board 댓글을 삭제할 board
     * SOFT DELETE 적용
     */
    @Modifying
    @Query("update BoardComment bc " +
            "set bc.deleted = true " +
            "where bc.board = :board")
    void deleteCommentAtBoard(@Param("board") Board board);

    /**
     * Board 의 id 정보를 리스트로 입력받아 각 Board 의 댓글 갯수 정보를 반환한다.
     * @param boardIds boardId Board Id 리스트 정보
     * @return 각 Board 의 댓글 수 리스트
     */
    @Query("select bc.board.id, count(bc) " +
            "from BoardComment bc " +
            "where bc.board.id in (:boardIds) " +
            "group by bc.board.id")
    List<Object[]> countAllByBoardIds(@Param("boardIds") List<Long> boardIds);

}
