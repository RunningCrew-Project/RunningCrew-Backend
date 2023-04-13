package com.project.runningcrew.blocked.reported.comment;

import com.project.runningcrew.blocked.reported.ReportType;
import com.project.runningcrew.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportedCommentRepository extends JpaRepository<ReportedComment, Long> {
    @Query("select rc.comment from ReportedComment rc where rc.reportType = :reportType")
    Slice<Comment> findByReportType(@Param("reportType") ReportType reportType, Pageable pageable);

}
