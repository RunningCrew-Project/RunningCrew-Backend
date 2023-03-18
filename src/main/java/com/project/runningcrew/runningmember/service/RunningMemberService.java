package com.project.runningcrew.runningmember.service;

import com.project.runningcrew.exception.AuthorizationException;
import com.project.runningcrew.runningmember.entity.RunningMember;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.exception.badinput.RunningDateTimeAfterException;
import com.project.runningcrew.exception.badinput.RunningPersonnelException;
import com.project.runningcrew.exception.alreadyExist.RunningMemberAlreadyExistsException;
import com.project.runningcrew.exception.notFound.RunningMemberNotFoundException;
import com.project.runningcrew.runningmember.repository.RunningMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * @throws RunningDateTimeAfterException 런닝시간 이후에 신청했을 때
     * @throws RunningPersonnelException 런닝 인원이 가득찼을 때
     * @throws RunningMemberAlreadyExistsException runningNotice 에 member 가 이미 참여했을 때
     */
    @Transactional
    public Long saveRunningMember(Member member, RunningNotice runningNotice) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(runningNotice.getRunningDateTime())) {
            throw new RunningDateTimeAfterException(now, runningNotice.getRunningDateTime());
        }

        Long memberCount = runningMemberRepository.countAllByRunningNotice(runningNotice);
        if (memberCount >= runningNotice.getRunningPersonnel()) {
            throw new RunningPersonnelException();
        }

        boolean isExist = runningMemberRepository.existsByMemberAndRunningNotice(member, runningNotice);
        if (isExist) {
            throw new RunningMemberAlreadyExistsException(runningNotice.getId());
        }

        RunningMember runningMember = new RunningMember(runningNotice, member);
        return runningMemberRepository.save(runningMember).getId();
    }

    /**
     * runningNotice 에 member 가 참여했는지 확인하고, 참여했다면 해당 runningMember 를 삭제한다.
     *
     * @param member
     * @param runningNotice
     * @throws AuthorizationException 참여 신청하는 member 가 runningNotice 의 작성자일 때
     * @throws RunningDateTimeAfterException 런닝시간 이후에 취소했을 때
     * @throws RunningMemberNotFoundException runningNotice 에 member 가 참여하지 않았을 때
     */
    @Transactional
    public void deleteRunningMember(Member member, RunningNotice runningNotice) {
        if (member.equals(runningNotice.getMember())) {
            throw new AuthorizationException();
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(runningNotice.getRunningDateTime())) {
            throw new RunningDateTimeAfterException(now, runningNotice.getRunningDateTime());
        }
        
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

    /**
     * RunningNotice 의 id 들을 받아, key 가 RunningNotice 의 id 이고 value 가 해당 RunningNotice 에
     * 참여한 RunningMember 수들인 Map 을 반환한다.
     *
     * @param runningNoticeIds RunningNotice 의 id 의 리스트
     * @return key 가 RunningNotice 의 id 이고 value 가 해당 RunningNotice 에 참여한 RunningMember 수들인 Map
     */
    public Map<Long, Long> countRunningMembersByRunningNoticeIds(List<Long> runningNoticeIds) {
        Map<Long, Long> countMap = runningMemberRepository
                .countRunningMembersByRunningNoticeIds(runningNoticeIds)
                .stream().collect(Collectors.toMap(o -> (Long) o[0], o -> (Long) o[1]));

        return runningNoticeIds.stream()
                .collect(Collectors.toMap(r -> r, r -> Objects.requireNonNullElse(countMap.get(r), 0L)));
    }

}
