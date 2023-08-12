package com.project.runningcrew.board.repository;

import com.project.runningcrew.board.entity.InfoBoard;
import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InfoBoardRepository extends JpaRepository<InfoBoard, Long> {

    /**
     * 특정 Crew 의 정보게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     * @param crew 크루 정보
     * @param member 정보를 조회하는 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 Crew 의 정보게시판 목록 조회(Dto 매핑)
     * SOFT DELETE 적용
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(ib.id, ib.createdDate, ib.title, u.nickname) " +
            "from InfoBoard ib " +
            "inner join Member m on m.crew = :crew and m.deleted = false " +
            "inner join User u on m.user = u and u.deleted = false " +
            "where ib.id not in ( " +
            "select ib.id from InfoBoard ib " +
            "inner join BlockedInfo bi on bi.blockedMemberId = ib.member.id and bi.deleted = false " +
            "where bi.blockerMember = :member ) " +
            "and ib.member = m")
    Slice<SimpleBoardDto> findInfoBoardDtoByCrew(@Param("crew") Crew crew, @Param("member") Member member, Pageable pageable);


    /**
     * 미사용 예정!!
     * 미사용 예정!!
     * 미사용 예정!!
     */
    @Query("select ib from InfoBoard ib where ib.member.crew = :crew")
    Slice<InfoBoard> findInfoBoardByCrew(@Param("crew") Crew crew, Pageable pageable);


}
