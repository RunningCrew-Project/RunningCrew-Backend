package com.project.runningcrew.repository;

import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningMemberRepository extends JpaRepository<RunningMember, Long> {

    List<RunningMember> findAllByMember(Member member);

    Long countAllByRunningNotice(RunningNotice runningNotice);

    List<RunningMember> findAllByRunningNotice(RunningNotice runningNotice);

}
