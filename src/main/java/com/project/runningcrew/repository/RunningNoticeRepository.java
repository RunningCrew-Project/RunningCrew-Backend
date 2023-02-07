package com.project.runningcrew.repository;

import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public interface RunningNoticeRepository extends JpaRepository<RunningNotice, Long> {


    /**
     * @param today
     * @param tomorrow
     * @return list of RunningNotice
     * 하루 간격의 날짜를 변수로 받아 그 날의 런닝 공지를 모두 반환한다.
     */
    @Query("select rn from RunningNotice rn where rn.runningDateTime between :today and :tomorrow")
    List<RunningNotice> findAllByRunningDate(@Param("today") LocalDate today, @Param("tomorrow") LocalDate tomorrow);


    /**
     * @param search
     * @return list of RunningNotice
     * 특정 단어 search 를 제목 or 내용에 포함하는 런닝 공지를 모두 반환한다.
     */
    @Query("select rn from RunningNotice rn where rn.title like %:search% and rn.detail like %:search%")
    List<RunningNotice> findAllByTitleAndDetail(@Param("search") String search);



    /**
     * @param noticeType
     * @param pageable
     * @return Page of RunningNotice
     * 특정 noticeType 을 변수로 받아서 해당되는 런닝 공지를 모두 반환한다.
     */
    @Query(value = "select rn from RunningNotice rn where rn.noticeType = :noticeType"
            , countQuery = "select count(rn) from RunningNotice rn")
    Page<RunningNotice> findAllTypeRunningNotice(@Param("noticeType") NoticeType noticeType, Pageable pageable ); // Paging

}
