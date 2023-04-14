package com.project.runningcrew.reported.comment;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.reported.ReportType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportedCommentRepository extends JpaRepository<ReportedComment, Long> {

    /**
     * 신고 사유를 받아 그에 해당되는 댓글 신고 정보 목록을 반환한다. - 페이징 적용
     * @param reportType 신고 사유
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 댓글 신고 정보 목록
     */
    @Query("select rc from ReportedComment rc where rc.member.crew = :crew and rc.reportType = :reportType")
    Slice<ReportedComment> findByCrewAndReportType(@Param("reportType") ReportType reportType, @Param("crew") Crew crew, Pageable pageable);

}
