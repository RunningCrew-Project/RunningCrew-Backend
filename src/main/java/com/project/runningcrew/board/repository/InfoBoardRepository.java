package com.project.runningcrew.board.repository;

import com.project.runningcrew.board.entity.InfoBoard;
import com.project.runningcrew.crew.entity.Crew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InfoBoardRepository extends JpaRepository<InfoBoard, Long> {


    /**
     * 특정 Crew 의 정보게시판 목록 paging 적용 출력
     * @param pageable
     * @return page of InfoBoard
     */
    @Query("select ib from InfoBoard ib where ib.member.crew = :crew")
    Slice<InfoBoard> findInfoBoardByCrew(@Param("crew") Crew crew, Pageable pageable);


}