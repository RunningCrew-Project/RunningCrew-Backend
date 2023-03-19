package com.project.runningcrew.member.service;

import com.project.runningcrew.board.repository.BoardRepository;
import com.project.runningcrew.comment.repository.CommentRepository;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.exception.badinput.UpdateMemberRoleException;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.recruitanswer.repository.RecruitAnswerRepository;
import com.project.runningcrew.resourceimage.repository.BoardImageRepository;
import com.project.runningcrew.resourceimage.repository.RunningNoticeImageRepository;
import com.project.runningcrew.runningmember.repository.RunningMemberRepository;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.runningnotice.repository.RunningNoticeRepository;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.alreadyExist.MemberAlreadyExistsException;
import com.project.runningcrew.exception.notFound.MemberNotFoundException;
import com.project.runningcrew.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RecruitAnswerRepository recruitAnswerRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final RunningMemberRepository runningMemberRepository;
    private final RunningNoticeRepository runningNoticeRepository;
    private final BoardImageRepository boardImageRepository;
    private final RunningNoticeImageRepository runningNoticeImageRepository;

    /**
     * 입력받은 id 를 가진 Member 를 찾아 반환한다. 없다면 MemberNotFoundException 을 throw 한다.
     *
     * @param memberId 찾는 Member 의 id
     * @return 입력받은 id 를 가진 Member
     * @throws MemberNotFoundException 입력받은 id 를 가진 Member 가 없을 때
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 입력받은 Member 의 가입여부를 확인하고, 가입하지 않았다면 저장하후 Member 에 부여된 id 를 반환한다.
     *
     * @param member 저장할 Member
     * @return Member 에 부여된 id
     * @throws MemberAlreadyExistsException user 가 crew 에 이미 가입했을 때
     */
    @Transactional
    public Long saveMember(Member member) {
        Optional<Member> optionalMember = memberRepository.findByUserAndCrew(member.getUser(), member.getCrew());
        if (optionalMember.isPresent()) {
            throw new MemberAlreadyExistsException(optionalMember.get().getId());
        }
        return memberRepository.save(member).getId();
    }

    /**
     * 입력받은 Member 의 가입여부를 확인하고, 가입하지 않았다면 Member 를 저장하고 Member 가 작성한
     * RecruitAnswer 을 삭제한 후, Member 에 부여된 id 를 반환한다.
     *
     * @param member 저장할 Member
     * @return Member 에 부여된 id
     * @throws MemberAlreadyExistsException user 가 crew 에 이미 가입했을 때
     */
    @Transactional
    public Long acceptMember(Member member) {
        Optional<Member> optionalMember = memberRepository.findByUserAndCrew(member.getUser(), member.getCrew());
        if (optionalMember.isPresent()) {
            throw new MemberAlreadyExistsException(optionalMember.get().getId());
        }

        recruitAnswerRepository.deleteByUserAndCrew(member.getUser(), member.getCrew());
        return memberRepository.save(member).getId();
    }

    /**
     * 입력받은 Member 를 삭제한다.
     *
     * @param member 삭제할 Member
     */
    @Transactional
    public void deleteMember(Member member) {
        boardImageRepository.deleteAllByMember(member);
        commentRepository.deleteAllByMember(member);
        boardRepository.deleteAllByMember(member);
        runningNoticeImageRepository.deleteAllByMember(member);
        runningMemberRepository.deleteAllByMember(member);
        runningNoticeRepository.deleteAllByMember(member);
        memberRepository.delete(member);
    }

    /**
     * role 이 MemberRole.ROLE_LEADER 인 leaderMember 의 role 을 MemberRole.ROLE_MANAGER 로 변경하고,
     * role 이 MemberRole.ROLE_MANAGER 인 updateMember 의 role 을 MemberRole.ROLE_LEADER 로 변경한다.
     *
     * @param leaderMember role 을 MemberRole.ROLE_LEADER 에서 MemberRole.ROLE_MANAGER 로 변경할 Member
     * @param updateMember role 을 MemberRole.ROLE_MANAGER 에서 MemberRole.ROLE_LEADER 로 변경할 Member
     */
    @Transactional
    public void updateMemberLeader(Member leaderMember, Member updateMember) {
        if (leaderMember.getRole() != MemberRole.ROLE_LEADER) {
            throw new UpdateMemberRoleException(leaderMember.getRole());
        }
        if (updateMember.getRole() != MemberRole.ROLE_MANAGER) {
            throw new UpdateMemberRoleException(updateMember.getRole());
        }

        leaderMember.updateRole(MemberRole.ROLE_MANAGER);
        updateMember.updateRole(MemberRole.ROLE_LEADER);
    }

    /**
     * role 이 MemberRole.ROLE_NORMAL 인 Member 의 role 을 MemberRole.ROLE_MANAGER 로 변경한다.
     *
     * @param member role 을 MemberRole.ROLE_NORMAL 에서 MemberRole.ROLE_MANAGER 로 변경할 Member
     */
    @Transactional
    public void updateMemberManager(Member member) {
        if (member.getRole() != MemberRole.ROLE_NORMAL) {
            throw new UpdateMemberRoleException(member.getRole());
        }
        member.updateRole(MemberRole.ROLE_MANAGER);
    }

    /**
     * role 이 MemberRole.ROLE_MANAGER 인 Member 의 role 을 MemberRole.ROLE_NORMAL 로 변경한다.
     *
     * @param member role 을 MemberRole.ROLE_MANAGER 에서 MemberRole.ROLE_NORMAL 로 변경할 Member
     */
    @Transactional
    public void updateMemberNormal(Member member) {
        if (member.getRole() != MemberRole.ROLE_MANAGER) {
            throw new UpdateMemberRoleException(member.getRole());
        }
        member.updateRole(MemberRole.ROLE_NORMAL);
    }

    /**
     * 입력받은 User 의 모든 Member 를 반환한다.
     *
     * @param user 찾는 Member 를 가지는 User
     * @return 입력받은 User 의 모든 Member
     */
    public List<Member> findAllByUser(User user) {
        return memberRepository.findAllByUser(user);
    }

    /**
     * 입력받은 Crew 에 속한 Member 의 수를 반환한다.
     *
     * @param crew Member 수를 확인할 Crew
     * @return 입력받은 Crew 에 속한 Member 의 수
     */
    public Long countAllByCrew(Crew crew) {
        return memberRepository.countAllByCrew(crew);
    }

    /**
     * 입력받은 Crew 에 속한 모든 Member 를 반환한다.
     *
     * @param crew Member 가 속한 Crew
     * @return 입력받은 Crew 에 속한 모든 Member
     */
    public List<Member> findAllByCrew(Crew crew) {
        return memberRepository.findAllByCrew(crew);
    }


    /**
     * 입력받은 Crew 의 관리자 Member 들을 모두 반환한다. 이때 관리자의 role 은 MemberRole.ROLE_LEADER
     * 또는 MemberRole.ROLE_LEADER 이다.
     *
     * @param crew Member 가 속한 Crew
     * @return 입력받은 Crew 의 모든 관리자 Member
     */
    public List<Member> findCrewMangers(Crew crew) {
        List<Member> leaders = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_LEADER);
        List<Member> managers = memberRepository.findAllByCrewAndRole(crew, MemberRole.ROLE_MANAGER);
        return Stream.concat(leaders.stream(), managers.stream()).collect(Collectors.toList());
    }

    /**
     * 특정 RunningNotice 에 참가한 모든 Member 를 반환
     *
     * @param runningNotice Member 들을 확인할 RunningNotice
     * @return 특정 RunningNotice 에 참가한 모든 Member 를 반환
     */
    public List<Member> findAllByRunningNotice(RunningNotice runningNotice) {
        return memberRepository.findAllByRunningNotice(runningNotice);
    }

    /**
     * 특정 user 와 crew 의 값을 가지는 Member 반환. 없다면 MemberNotFoundException 을 throw 한다.
     *
     * @param user
     * @param crew
     * @return 특정 user 와 crew 의 값을 가지는 Member
     * @throws MemberNotFoundException 특정 user 와 crew 의 값을 가지는 Member 가 없을 때
     */
    public Member findByUserAndCrew(User user, Crew crew) {
        return memberRepository.findByUserAndCrew(user, crew)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * Crew id 의 리스트를 받아, key 가 Crew 의 id 이고 value 가 Crew 의 멤버수인 map 을 반환한다.
     *
     * @param crewIds Crew 의 id 리스트
     * @return ey 가 Crew 의 id 이고 value 가 Crew 의 멤버수인 map
     */
    public Map<Long, Long> countAllByCrewIds(List<Long> crewIds) {
        return memberRepository.countAllByCrewIds(crewIds).stream()
                .collect(Collectors.toMap(o -> (Long) o[0], o -> (Long) o[1]));
    }
    
}
