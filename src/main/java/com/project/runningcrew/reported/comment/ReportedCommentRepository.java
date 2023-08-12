package com.project.runningcrew.reported.comment;

import com.project.runningcrew.crew.entity.Crew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportedCommentRepository extends JpaRepository<ReportedComment, Long> {

    /**
     * 입력받은 크루의 댓글 신고 정보 전체 목록을 반환한다.
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 댓글 신고 정보 목록
     * SOFT DELETE 적용
     */
    @Query("select rc " +
            "from ReportedComment rc " +
            "inner join Member m on rc.member = m and m.deleted = false " +
            "inner join Crew c on m.crew = c and c.deleted = false " +
            "where c = :crew")
    Slice<ReportedComment> findByCrew(@Param("crew") Crew crew, Pageable pageable);

}
