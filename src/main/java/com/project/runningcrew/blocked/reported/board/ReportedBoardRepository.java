package com.project.runningcrew.blocked.reported.board;

import com.project.runningcrew.blocked.reported.ReportType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportedBoardRepository extends JpaRepository<ReportedBoard, Long> {

    /**
     * 신고 사유를 받아 그에 해당되는 게시글 신고 정보 목록을 반환한다. - 페이징 적용
     * @param reportType 신고 사유
     * @param pageable 페이징 정보
     * @return 신고 정보 목록
     */
    Slice<ReportedBoard> findByReportType(ReportType reportType, Pageable pageable);

}
