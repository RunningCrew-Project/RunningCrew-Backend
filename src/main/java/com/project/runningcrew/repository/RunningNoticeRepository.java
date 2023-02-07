package com.project.runningcrew.repository;

import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RunningNoticeRepository extends JpaRepository<RunningNotice, Long> {


    List<RunningNotice> findAllByRunningDateTime();

    List<RunningNotice> findAllByTitleAndDetail();


    /**
     *
     * @param noticeType
     * @param pageable
     * @return Page of RunningNotice
     * 특정 noticeType 을 변수로 받아서 해당되는 게시물을 모두 반환한다.
     */
    @Query(value = "select rn from RunningNotice rn where rn.noticeType = :noticeType"
            , countQuery = "select count(rn) from RunningNotice rn")
    Page<RunningNotice> findAllTypeRunningNotice(@Param("noticeType") NoticeType noticeType, Pageable pageable ); // Paging

}
