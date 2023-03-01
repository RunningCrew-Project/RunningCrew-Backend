package com.project.runningcrew.service;

import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.exception.RunningDateTimeException;
import com.project.runningcrew.exception.RunningPersonnelException;
import com.project.runningcrew.exception.alreadyExist.RunningMemberAlreadyExistsException;
import com.project.runningcrew.exception.notFound.RunningMemberNotFoundException;
import com.project.runningcrew.repository.RunningMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningMemberService {

    private final RunningMemberRepository runningMemberRepository;

    /**
     * runningNotice 에 member 가 참여했는지 확인하고, 참여하지 않았다면 runningMember 를 생성 후
     * runningMember 에 부여된 id 를 반환한다.
     *
     * @param member
     * @param runningNotice
     * @return 생성된 runningMember 의 id
     * @throws RunningDateTimeException 런닝시간 이후에 신청했을 때
     * @throws RunningPersonnelException 런닝 인원이 가득찼을 때
     * @throws RunningMemberAlreadyExistsException runningNotice 에 member 가 이미 참여했을 때
     */
    @Transactional
    public Long saveRunningMember(Member member, RunningNotice runningNotice) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(runningNotice.getRunningDateTime())) {
            throw new RunningDateTimeException();
        }

        Long memberCount = runningMemberRepository.countAllByRunningNotice(runningNotice);
        if (memberCount >= runningNotice.getRunningPersonnel()) {
            throw new RunningPersonnelException();
        }

        boolean isExist = runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice);
        if (isExist) {
            throw new RunningMemberAlreadyExistsException();
        }

        RunningMember runningMember = new RunningMember(runningNotice, member);
        return runningMemberRepository.save(runningMember).getId();
    }

    /**
     * runningNotice 에 member 가 참여했는지 확인하고, 참여했다면 해당 runningMember 를 삭제한다.
     *
     * @param member
     * @param runningNotice
     * @throws RunningMemberNotFoundException runningNotice 에 member 가 참여하지 않았을 때
     */
    @Transactional
    public void deleteRunningMember(Member member, RunningNotice runningNotice) {
        RunningMember runningMember = runningMemberRepository
                .findByMemberAndRunningNotice(member, runningNotice)
                .orElseThrow(RunningMemberNotFoundException::new);
        runningMemberRepository.delete(runningMember);
    }

    /**
     * runningnotice 에 참여한 member 수를 반환한다.
     *
     * @param runningNotice
     * @return runningNotice 에 참여한 member 수
     */
    public Long countAllByRunningNotice(RunningNotice runningNotice) {
        return runningMemberRepository.countAllByRunningNotice(runningNotice);
    }

    /**
     * memeber 의 runningNotice 참여 여부를 반환한다.
     *
     * @param member
     * @param runningNotice
     * @return runningNotice 에 member 가 참여했다면 true, 참여하지 않았다면 false
     */
    public boolean existsByMemberAndRunningNotice(Member member, RunningNotice runningNotice) {
        return runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice);
    }

}
