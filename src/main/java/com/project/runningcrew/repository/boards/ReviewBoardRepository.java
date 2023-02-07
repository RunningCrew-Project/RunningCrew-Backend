package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.boards.ReviewBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

    /**
     * @param pageable
     * @return page of ReviewBoard
     * 리뷰게시판 전체 게시물 출력에 paging 을 적용한다.
     */
    @Query("select rb from ReviewBoard rb")
    Page<FreeBoard> findReviewBoardAll(Pageable pageable);

}
