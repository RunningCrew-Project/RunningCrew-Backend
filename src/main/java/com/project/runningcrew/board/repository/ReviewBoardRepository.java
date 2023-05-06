package com.project.runningcrew.board.repository;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.ReviewBoard;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {


    /**
     * 특정 Crew 의 리뷰게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     * @param crew 크루 정보
     * @param member 정보를 조회하는 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 Crew 의 리뷰게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(rb.id, rb.createdDate, rb.title, u.nickname) " +
            "from ReviewBoard rb " +
            "inner join Member m on m.crew = :crew " +
            "inner join User u on m.user = u " +
            "where rb.id not in " +
            "(select rb.id from ReviewBoard rb inner join BlockedInfo bi on bi.blockedMemberId = rb.member.id where bi.blockerMember = :member) " +
            "and rb.member = m")
    Slice<SimpleBoardDto> findReviewBoardDtoByCrew(@Param("crew") Crew crew, @Param("member") Member member, Pageable pageable);


    /**
     * 미사용 예정!!
     * 미사용 예정!!
     * 미사용 예정!!
     */
    @Query("select rb from ReviewBoard rb where rb.member.crew = :crew")
    Slice<ReviewBoard> findReviewBoardByCrew(@Param("crew") Crew crew, Pageable pageable);


}
