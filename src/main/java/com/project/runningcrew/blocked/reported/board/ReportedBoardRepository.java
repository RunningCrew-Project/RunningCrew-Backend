package com.project.runningcrew.blocked.reported.board;

import com.project.runningcrew.blocked.reported.ReportType;
import com.project.runningcrew.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportedBoardRepository extends JpaRepository<ReportedBoard, Long> {

    /**
     * 신고 사유를 받아 그에 해당되는 게시글을 반환한다. - 페이징 적용
     * @param reportType 신고 사유
     * @return 신고 사유에 따른 ReportedBoard 의 Slice
     */
    @Query("select rb.board from ReportedBoard rb where rb.reportType = :reportType")
    Slice<Board> findByReportType(@Param("reportType") ReportType reportType, Pageable pageable);

}
