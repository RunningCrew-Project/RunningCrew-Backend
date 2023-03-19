package com.project.runningcrew.member.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.exception.AuthorizationException;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthorizationChecker {

    private final MemberRepository memberRepository;

    /**
     * 특정 크루에 특정 유저가 가입했는지 확인하고, 가입했다면 해당 멤버를 반환한다.
     *
     * @param user
     * @param crew
     * @return user 와 crew 를 포함하는 member
     * @throws AuthorizationException user 와 crew 를 포함한 member 가 없을 때
     */
    public Member checkMember(User user, Crew crew) {
        return memberRepository.findByUserAndCrew(user, crew).orElseThrow(AuthorizationException::new);
    }

    /**
     * 특정 크루에 특정 유저가 가입했고 권한이 매니저 이상인지 확인하고, 조건을 만족한다면 member 를 반환한다.
     *
     * @param user
     * @param crew
     * @return user 와 crew 를 포함하고, 권한이 매니저인 member
     * @throws AuthorizationException 권한이 매니저가 아닐 때
     */
    public Member checkManager(User user, Crew crew) {
        Member currentMember = checkMember(user, crew);
        if (!currentMember.getRole().isManager()) {
            throw new AuthorizationException();
        }
        return currentMember;
    }

    /**
     * 특정 크루에 특정 유저가 가입했고 권한이 리더인지 확인하고, 조건을 만족한다면 member 를 반환한다.
     *
     * @param user
     * @param crew
     * @return user 와 crew 를 포함하고, 권한이 리더인 member
     * @throws AuthorizationException 권한이 리더가 아닐 때
     */
    public Member checkLeader(User user, Crew crew) {
        Member currentMember = checkMember(user, crew);
        if (currentMember.getRole() != MemberRole.ROLE_LEADER) {
            throw new AuthorizationException();
        }
        return currentMember;
    }

    /**
     * 특정 크루에 특정 유저가 가입했는지 확인하고, 입력받은 memberId 를 가진다면 member 를 반환한다.
     *
     * @param user
     * @param crew
     * @param memberId 권한을 확인할 엔티티의 memberId
     * @return user 와 crew 를 포함하고 id 가 memberId 인 member
     * @throws AuthorizationException member 의 id 가 memberId 가 아닐 때
     */
    public Member checkAuthOnlyUser(User user, Crew crew, Long memberId) {
        Member currentMember = checkMember(user, crew);
        if (!currentMember.getId().equals(memberId)) {
            throw new AuthorizationException();
        }
        return currentMember;
    }

    /**
     * 특정 크루에 특정 유저가 가입했는지 확인하고, 입력받은 memberId 를 가지거나 권한이 매니저 이상이면 member 를 반환한다.
     *
     * @param user
     * @param crew
     * @param memberId 권한을 확인할 엔티티의 memberId
     * @return user 와 crew 를 포함하고 id 가 memberId 이거나 권한이 매니저 이상인 member
     * @throws AuthorizationException member 의 id 가 memberId 가 아니고 권한이 매니저 이상이 아닐 때
     */
    public Member checkAuthUserAndManger(User user, Crew crew, Long memberId) {
        Member currentMember = checkMember(user, crew);
        if (!currentMember.getId().equals(memberId) && !currentMember.getRole().isManager()) {
            throw new AuthorizationException();
        }
        return currentMember;
    }

    /**
     * 특정 크루에 특정 유저가 가입했는지 확인하고, 입력받은 memberId 를 가지거나 권한이 리더이면 member 를 반환한다.
     *
     * @param user
     * @param crew
     * @param memberId 권한을 확인할 엔티티의 memberId
     * @return user 와 crew 를 포함하고 id 가 memberId 이거나 권한이 리더인 member
     * @throws AuthorizationException member 의 id 가 memberId 가 아니고 권한이 리더가 아닐 때
     */
    public Member checkAuthUserAndLeader(User user, Crew crew, Long memberId) {
        Member currentMember = checkMember(user, crew);
        if (!currentMember.getId().equals(memberId) && currentMember.getRole() != MemberRole.ROLE_LEADER) {
            throw new AuthorizationException();
        }
        return currentMember;
    }

}
