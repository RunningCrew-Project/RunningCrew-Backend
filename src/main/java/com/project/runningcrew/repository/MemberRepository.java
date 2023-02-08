package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 특정 user 에 속한 member 들의 list 반환
     * @param user 찾는 member 를 가지는 user
     * @return user 에 속한 member 들의 list
     */
    List<Member> findAllByUser(User user);

    /**
     * 특정 crew 에 가입한 member 수를 반환
     * @param crew member 수를 찾을 crew
     * @return crew 에 가입한 member 들의 수
     */
    Long countAllByCrew(Crew crew);

    /**
     * 특정 crew 에 가입한 member 들의 list 반환
     * @param crew 찾는 member 들이 가입한 crew
     * @return crew 에 가입한 member 들의 list
     */
    List<Member> findAllByCrew(Crew crew);

    /**
     * 특정 crew 에 가입하고 특정 role 을 가지는 member 들의 list 반환
     * @param crew 찾는 member 들이 가입한 crew
     * @param role 찾는 member 들의 role
     * @return crew 에 가입한 member 중 role 을 가지는 member 들의 list
     */
    List<Member> findAllByCrewAndRole(Crew crew, MemberRole role);

}
