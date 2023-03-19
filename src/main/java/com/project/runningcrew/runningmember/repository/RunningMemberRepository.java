package com.project.runningcrew.runningmember.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.runningmember.entity.RunningMember;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RunningMemberRepository extends JpaRepository<RunningMember, Long> {

    /**
     * member 를 포함하는 모든 RunningMember 반환
     *
     * @param member
     * @return member 를 포함된 모든 RunningMember 의 list
     */
    List<RunningMember> findAllByMember(Member member);

    /**
     * runningNotice 를 포함하는 모든 RunningMember 의 개수 반환
     *
     * @param runningNotice
     * @return runningNotice 를 포함하는 모든 RunningMember 의 개수
     */
    Long countAllByRunningNotice(RunningNotice runningNotice);

    /**
     * runningNotice 를 포함하는 모든 RunningMember 반환
     *
     * @param runningNotice
     * @return runningNotice 를 포함하는 모든 RunningMember 의 list
     */
    List<RunningMember> findAllByRunningNotice(RunningNotice runningNotice);

    /**
     * Member 와 RunningNotice 를 포함하는 RunningMember 반환
     *
     * @param member
     * @param runningNotice
     * @return Member 와 RunningNotice 를 포함하는 RunningMember 반환. 없다면 Option.empty() 반환
     */
    Optional<RunningMember> findByMemberAndRunningNotice(Member member, RunningNotice runningNotice);

    /**
     * Member 의 RunningNotice 참여여부 반환
     *
     * @param member
     * @param runningNotice
     * @return member 가 runningNotice 에 참여했다면 true, 아니라면 false
     */
    boolean existsByMemberAndRunningNotice(Member member, RunningNotice runningNotice);


    /**
     * RunningNotice 를 포함하는 모든 RunningMember 삭제
     *
     * @param runningNotice
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from RunningMember r where r.runningNotice = :runningNotice")
    void deleteAllByRunningNotice(@Param("runningNotice") RunningNotice runningNotice);

    /**
     * Member 를 포함하는 모든 RunningMember 삭제
     *
     * @param member
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from RunningMember r where r.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * Crew 에 포함되는 모든 RunningMember 삭제
     *
     * @param crew
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from RunningMember r1 where r1 in " +
            "(select r2 from RunningMember r2 where r2.member.crew = :crew)")
    void deleteAllByCrew(@Param("crew") Crew crew);

    /**
     * runningNoticeId 의 리스트에 포함된 id 를 가지는 RunningNotice 들의 런닝멤버수를 반환
     *
     * @param runningNoticeIds RunningNotice 의 id 의 리스트
     * @return runningNoticeId 와 RunningNotice 의 멤버수가 담긴 Object[] 의 리스트. Object[0] 에는 Long 타입인
     * runningNoticeId, Object[1] 에는  Long 타입인 RunningNotice 의 RunningMember 수
     */
    @Query("select r.runningNotice.id, count(r) from RunningMember r where r.runningNotice.id in (:runningNoticeIds) group by r.runningNotice")
    List<Object[]> countRunningMembersByRunningNoticeIds(@Param("runningNoticeIds") List<Long> runningNoticeIds);

}
