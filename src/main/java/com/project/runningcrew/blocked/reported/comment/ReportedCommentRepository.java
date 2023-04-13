package com.project.runningcrew.blocked.reported.comment;

import com.project.runningcrew.blocked.reported.ReportType;
import com.project.runningcrew.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReportedCommentRepository extends JpaRepository<ReportedComment, Long> {

    /**
     * 신고 사유를 받아 그에 해당되는 댓글 신고 정보 목록을 반환한다. - 페이징 적용
     * @param reportType 신고 사유
     * @param pageable 페이징 정보
     * @return 댓글 신고 정보 목록
     */
    Slice<ReportedComment> findByReportType(@Param("reportType") ReportType reportType, Pageable pageable);

}
