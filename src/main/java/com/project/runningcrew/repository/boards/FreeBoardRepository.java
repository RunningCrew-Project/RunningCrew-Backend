package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    /**
     * 특정 Crew 의 자유게시판 목록 paging 적용 출력
     * @param pageable
     * @return page of FreeBoard
     */
    @Query("select fb from FreeBoard fb where fb.member.crew = :crew")
    Slice<FreeBoard> findFreeBoardByCrew(@Param("crew") Crew crew, Pageable pageable);

}
