package com.project.runningcrew.repository;

import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
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

}
