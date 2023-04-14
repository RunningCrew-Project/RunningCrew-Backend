package com.project.runningcrew.reported.board;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.reported.ReportType;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReportedBoardRepository extends JpaRepository<ReportedBoard, Long> {

    /**
     * 신고 사유를 받아 그에 해당되는 게시글 신고 정보 목록을 반환한다. - 페이징 적용
     * @param reportType 신고 사유
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 신고 정보 목록
     */
    @Query("select rb from ReportedBoard rb where rb.member.crew = :crew and rb.reportType = :reportType")
    Slice<ReportedBoard> findByCrewAndReportType(@Param("reportType") ReportType reportType, @Param("crew") Crew crew, Pageable pageable);

}
