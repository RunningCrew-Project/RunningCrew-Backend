package com.project.runningcrew.board.repository;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.ReviewBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

    /**
     * 특정 Crew 의 리뷰게시판 목록 paging 적용 출력
     * @param pageable
     * @return page of ReviewBoard
     */
    @Query("select rb from ReviewBoard rb where rb.member.crew = :crew")
    Slice<ReviewBoard> findReviewBoardByCrew(@Param("crew") Crew crew, Pageable pageable);

    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(rb.id, rb.createdDate, rb.title, u.nickname) " +
            "from ReviewBoard rb " +
            "inner join Member m on m.crew = :crew " +
            "inner join User u on m.user = u")
    Slice<SimpleBoardDto> findReviewBoardDtoByCrew(@Param("crew") Crew crew, Pageable pageable);
}
