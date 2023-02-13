package com.project.runningcrew.repository;

import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RunningNoticeRepository extends JpaRepository<RunningNotice, Long> {


    /**
     * 하루 간격의 날짜를 변수로 받아 그 날의 런닝 공지를 모두 반환한다.
     * @param today
     * @param tomorrow
     * @return list of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.runningDateTime between :today and :tomorrow")
    List<RunningNotice> findAllByRunningDate(@Param("today") LocalDateTime today, @Param("tomorrow") LocalDateTime tomorrow);


    /**
     * 특정 단어 search 를 제목 or 내용에 포함하는 런닝 공지를 모두 반환한다.
     * @param keyword
     * @return list of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.title like %:keyword% or rn.detail like %:keyword%")
    List<RunningNotice> findAllByTitleOrDetail(@Param("keyword") String keyword);


    /**
     * 특정 Member 가 작성한 런닝 공지를 모두 반환한다.
     * @param member
     * @return list of RunningNotice
     */
    List<RunningNotice> findAllByMember(Member member);



    /**
     * 특정 noticeType 을 변수로 받아서 해당되는 런닝 공지를 모두 반환한다.
     * @param noticeType
     * @param pageable
     * @return Slice of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.noticeType = :noticeType")
    Slice<RunningNotice> findAllByNoticeType (@Param("noticeType") NoticeType noticeType, Pageable pageable );


}
