package com.project.runningcrew.resourceimage.repository;

import com.project.runningcrew.board.entity.Board;
import com.project.runningcrew.resourceimage.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    /**
     * board 에 포함된 모든 BoardImage 반환
     *
     * @param board
     * @return board 에 포함된 모든 BoardImage 의 list
     */
    List<BoardImage> findAllByBoard(Board board);

    /**
     * board 에 포함된 모든 BoardImage 삭제
     *
     * @param board
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from BoardImage b where b.board = :board")
    void deleteAllByBoard(@Param("board") Board board);

    /**
     * boardId 의 리스트에 포함된 boardId 를 가진 BoardImage 반환
     *
     * @param boardIds Board 의 id 를 가진 리스트
     * @return Board 의 id 가 boardIds 에 포함된 모든 BoardImage.
     */
    @Query("select b from BoardImage b where b.board.id in (:boardIds)")
    List<BoardImage> findImagesByBoardIds(@Param("boardIds") List<Long> boardIds);

}
