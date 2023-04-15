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
     * 입력받은 크루의 게시글 신고 정보 전체 목록을 반환한다. - 페이징 적용
     * @param crew 크루
     * @param pageable 페이징 정보
     * @return 신고 정보 목록
     */
    @Query("select rb from ReportedBoard rb where rb.member.crew = :crew")
    Slice<ReportedBoard> findByCrew(@Param("crew") Crew crew, Pageable pageable);

}
