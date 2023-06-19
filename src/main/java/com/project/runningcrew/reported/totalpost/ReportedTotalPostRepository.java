package com.project.runningcrew.reported.totalpost;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportedTotalPostRepository extends JpaRepository<ReportedTotalPost, Long> {

    /**
     * 입력받은 크루의 신고글 정보 전체 목록을 반환한다. - 페이징 적용
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 신고글 정보 목록
     */
    @Query("select rp from ReportedTotalPost rp where rp.member.crew = :crew")
    Slice<ReportedTotalPost> findByCrew(@Param("crew") Crew crew, Pageable pageable);

    /**
     * 입력받은 멤버가 작성한 신고글 정보 전체 목록을 삭제한다
     * @param member 멤버 정보
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ReportedTotalPost rp where rp.member = :member")
    void deleteAllByMember(@Param("member") Member member);

}
