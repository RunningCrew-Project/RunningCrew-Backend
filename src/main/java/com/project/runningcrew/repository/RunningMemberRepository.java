package com.project.runningcrew.repository;

import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningMemberRepository extends JpaRepository<RunningMember, Long> {

    /**
     * member 를 포함하는 모든 RunningMember 반환
     * @param member
     * @return member 를 포함된 모든 RunningMember 의 list
     */
    List<RunningMember> findAllByMember(Member member);

    /**
     * runningNotice 를 포함하는 모든 RunningMember 의 개수 반환
     * @param runningNotice
     * @return runningNotice 를 포함하는 모든 RunningMember 의 개수
     */
    Long countAllByRunningNotice(RunningNotice runningNotice);

    /**
     * runningNotice 를 포함하는 모든 RunningMember 반환
     * @param runningNotice
     * @return runningNotice 를 포함하는 모든 RunningMember 의 list
     */
    List<RunningMember> findAllByRunningNotice(RunningNotice runningNotice);

}
