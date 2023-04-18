package com.project.runningcrew.board.repository;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.FreeBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    /**
     * 특정 Crew 의 자유게시판 목록 조회 - 페이징 적용
     * @param pageable
     * @return page of FreeBoard
     */
    @Query("select fb from FreeBoard fb where fb.member.crew = :crew")
    Slice<FreeBoard> findFreeBoardByCrew(@Param("crew") Crew crew, Pageable pageable);


    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(fb.id, fb.createdDate, fb.title, u.nickname) " +
            "from FreeBoard fb " +
            "inner join Member m on m.crew = :crew " +
            "inner join User u on m.user = u")
    Slice<SimpleBoardDto> findFreeBoardDtoByCrew(@Param("crew") Crew crew, Pageable pageable);

}
