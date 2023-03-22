package com.project.runningcrew.runningnotice.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.NoticeType;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.entity.RunningStatus;
import com.project.runningcrew.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RunningNoticeRepository extends JpaRepository<RunningNotice, Long> {


    /**
     * 특정 Crew 에서 특정 날짜 사이에 런닝을 시행한 런닝공지를 모두 반환한다.
     *
     * @param start
     * @param end
     * @return list of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.member.crew = :crew and " +
            "rn.runningDateTime >= :start and rn.runningDateTime < :end")
    List<RunningNotice> findAllByCrewAndRunningDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("crew") Crew crew);

    /**
     * 특정 Crew 에서 특정 날짜 사이에 런닝을 시행한 특정 종류의 런닝공지를 모두 반환한다.
     *
     * @param start
     * @param end
     * @return list of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.member.crew = :crew and " +
            "rn.noticeType = :noticeType and " +
            "rn.runningDateTime >= :start and rn.runningDateTime < :end")
    List<RunningNotice> findAllByCrewAndRunningDateAndNoticeType(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                                 @Param("crew") Crew crew, @Param("noticeType") NoticeType noticeType);


    /**
     * 특정 Crew 의  keyword 를 제목 or 내용에 포함하는 런닝 공지를 모두 반환한다.
     *
     * @param keyword
     * @param crew
     * @return list of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.member.crew = :crew and (rn.title like %:keyword% or rn.detail like %:keyword%)")
    List<RunningNotice> findListAllByCrewAndKeyWord(@Param("keyword") String keyword, @Param("crew") Crew crew);


    /**
     * 특정 Crew 의  keyword 를 제목 or 내용에 포함하는 런닝 공지를 모두 반환한다.(paging 적용)
     *
     * @param keyword
     * @param crew
     * @return slice of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.member.crew = :crew and (rn.title like %:keyword% or rn.detail like %:keyword%)")
    Slice<RunningNotice> findSliceAllByCrewAndKeyWord(@Param("keyword") String keyword, @Param("crew") Crew crew, Pageable pageable);


    /**
     * 특정 Member 가 작성한 런닝 공지를 모두 반환한다.
     *
     * @param member
     * @return list of RunningNotice
     */
    Slice<RunningNotice> findAllByMember(Member member, Pageable pageable);

    /**
     * 특정 Crew 의 모든 런닝 공지를 반환한다.
     *
     * @param crew
     * @return 특정 Crew 의 모든 런닝 공지
     */
    @Query("select rn from RunningNotice rn where rn.member.crew = :crew")
    List<RunningNotice> findAllByCrew(@Param("crew") Crew crew);

    /**
     * 특정 RunningStatus 의 RunningNotice 를 RunningDateTime 순으로 정렬하여 출력한다.
     *
     * @param status
     * @return list of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.member.crew = :crew and rn.status = :status order by  rn.runningDateTime")
    List<RunningNotice> findAllByCrewAndStatus(@Param("status") RunningStatus status, @Param("crew") Crew crew);


    /**
     * 특정 noticeType 을 변수로 받아서 해당되는 런닝 공지를 모두 반환한다.
     *
     * @param noticeType
     * @param pageable
     * @return Slice of RunningNotice
     */
    @Query("select rn from RunningNotice rn where rn.noticeType = :noticeType and rn.member.crew = :crew")
    Slice<RunningNotice> findAllByCrewAndNoticeType(@Param("noticeType") NoticeType noticeType, @Param("crew") Crew crew, Pageable pageable);


    /**
     * 특정 유저가 신청한 특정 상태의 런닝 공지를 모두 반환한다.
     *
     * @param user
     * @param status
     * @return 특정 유저가 신청한 특정 상태의 모든 러닝 공지
     */
    @Query("select rm.runningNotice from RunningMember rm " +
            "where rm.member.user = :user and rm.runningNotice.status = :status")
    List<RunningNotice> findAllByUserAndStatus(@Param("user") User user, @Param("status") RunningStatus status);

    /**
     * member 가 생성한 모든 RunningNotice 삭제
     *
     * @param member
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RunningNotice r where r.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * crew 에 포함된 모든 RunningNotice 삭제
     *
     * @param crew
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RunningNotice r1 where r1 in " +
            "(select r2 from RunningNotice r2 where r2.member.crew = :crew)")
    void deleteAllByCrew(@Param("crew") Crew crew);

    /**
     * 특정 Member 의 RunningMember 들에 포함된 RunningNotice 반환
     *
     * @param member 런닝에 참가한 멤버
     * @return 특정 Member 의 RunningMember 들에 포함된 RunningNotice
     */
    @Query("select r.runningNotice from RunningMember r where r.member = :member")
    Slice<RunningNotice> findRunningNoticesByApplyMember(@Param("member") Member member, Pageable pageable);

}
