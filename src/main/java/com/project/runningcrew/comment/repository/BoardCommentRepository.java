package com.project.runningcrew.comment.repository;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.comment.entity.BoardComment;
import com.project.runningcrew.common.dto.SimpleCommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {


    /**
     * 입력받은 Board 에 작성된 댓글 리스트를 반환한다.
     * @param board 입력받은 board
     * @return 입력받은 Board 의 Comment -> SimpleCommentDto 리스트
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleCommentDto(bc.id, m.id, bc.createdDate, bc.detail, u.nickname, u.imgUrl) " +
            "from BoardComment bc " +
            "inner join Board b on b = bc.board " +
            "inner join Member m on m = bc.member " +
            "inner join User u on m.user = u " +
            "where bc.board = :board")
    List<SimpleCommentDto> findAllByBoard(@Param("board") Board board);


    /**
     * 입력받은 Board 에 작성된 댓글을 모두 삭제한다.
     * @param board 댓글을 삭제할 board
     */
    @Modifying
    @Query("delete from BoardComment bc where bc.board = :board")
    void deleteCommentAtBoard(@Param("board") Board board);


    /**
     * Board 의 id 정보를 리스트로 입력받아 각 Board 의 댓글 갯수 정보를 반환한다.
     * @param boardIds boardId Board Id 리스트 정보
     * @return 각 Board 의 댓글 수 리스트
     */
    @Query("select bc.board.id, count(bc) from BoardComment bc where bc.board.id in (:boardIds) group by bc.board.id")
    List<Object[]> countAllByBoardIds(@Param("boardIds") List<Long> boardIds);









    /**
     * 미사용 예정, Test 코드 컴파일 오류때문에 남겨둠.
     * 미사용 예정, Test 코드 컴파일 오류때문에 남겨둠.
     * 미사용 예정, Test 코드 컴파일 오류때문에 남겨둠.
     */
    @Query("select count(bc) from BoardComment bc where bc.board.id in (:boardId) group by bc.board.id")
    List<Integer> countByBoardId(@Param("boardId") List<Long> boardId);




}
