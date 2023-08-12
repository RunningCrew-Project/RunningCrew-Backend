package com.project.runningcrew.board.repository;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {


    /**
     * 특정 Crew 의 자유게시판 SimpleBoardDto 목록 조회
     * @param crew 크루 정보
     * @param member 목록을 조회할 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 Crew 의 자유게시판 SimpleBoardDto 목록 조회
     * SOFT DELETE 적용
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(fb.id, fb.createdDate, fb.title, u.nickname) " +
            "from FreeBoard fb " +
            "inner join Member m on m.crew = :crew and m.deleted = false " +
            "inner join User u on m.user = u and u.deleted = false " +
            "where fb.id not in ( " +
            "select fb.id " +
            "from FreeBoard fb " +
            "inner join BlockedInfo bi on bi.blockedMemberId = fb.member.id and bi.deleted = false " +
            "where bi.blockerMember = :member ) " +
            "and fb.member = m")
    Slice<SimpleBoardDto> findFreeBoardDtoByCrew(@Param("crew") Crew crew, @Param("member") Member member, Pageable pageable);



    /**
     * 미사용 예정!!
     * 미사용 예정!!
     * 미사용 예정!!
     */
    @Query("select fb from FreeBoard fb where fb.member.crew = :crew")
    Slice<FreeBoard> findFreeBoardByCrew(@Param("crew") Crew crew, Pageable pageable);



}
