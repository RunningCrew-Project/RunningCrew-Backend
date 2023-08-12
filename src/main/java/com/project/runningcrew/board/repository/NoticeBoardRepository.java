package com.project.runningcrew.board.repository;

import com.project.runningcrew.common.dto.SimpleBoardDto;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.board.entity.NoticeBoard;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {


    /**
     * 특정 Crew 의 공지게시판 목록 조회(Dto 매핑) - 페이징 & 차단 적용
     * @param crew 크루 정보
     * @param member 정보를 조회하는 멤버 정보
     * @param pageable 페이징 정보
     * @return 특정 Crew 의 공지게시판 목록 조회(Dto 매핑)
     *
     * SOFT DELETE 적용
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleBoardDto(nb.id, nb.createdDate, nb.title, u.nickname) " +
            "from NoticeBoard nb " +
            "inner join Member m on m.crew = :crew and m.deleted = false " +
            "inner join User u on m.user = u and u.deleted = false " +
            "where nb.id not in ( " +
            "select nb.id from NoticeBoard nb " +
            "inner join BlockedInfo bi on bi.blockedMemberId = nb.member.id and bi.deleted = false " +
            "where bi.blockerMember = :member ) " +
            "and nb.member = m")
    Slice<SimpleBoardDto> findNoticeBoardDtoByCrew(@Param("crew") Crew crew, @Param("member") Member member, Pageable pageable);



    /**
     * 미사용 예정!!
     * 미사용 예정!!
     * 미사용 예정!!
     */
    @Query("select nb from NoticeBoard nb where nb.member.crew = :crew")
    Slice<NoticeBoard> findNoticeBoardByCrew(@Param("crew") Crew crew, Pageable pageable);

}
