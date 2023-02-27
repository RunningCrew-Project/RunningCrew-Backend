package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.images.BoardImage;
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

}
