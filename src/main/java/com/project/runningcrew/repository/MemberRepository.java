package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Override
    @EntityGraph(attributePaths = {"user"})
    Optional<Member> findById(Long aLong);

    /**
     * 특정 user 에 속한 member 들의 list 반환
     *
     * @param user 찾는 member 를 가지는 user
     * @return user 에 속한 member 들의 list
     */
    List<Member> findAllByUser(User user);

    /**
     * 특정 crew 에 가입한 member 수를 반환
     *
     * @param crew member 수를 찾을 crew
     * @return crew 에 가입한 member 들의 수
     */
    Long countAllByCrew(Crew crew);

    /**
     * 특정 crew 에 가입한 member 들의 list 반환
     *
     * @param crew 찾는 member 들이 가입한 crew
     * @return crew 에 가입한 member 들의 list
     */
    @EntityGraph(attributePaths = {"user"})
    List<Member> findAllByCrew(Crew crew);

    /**
     * 특정 crew 에 가입하고 특정 role 을 가지는 member 들의 list 반환
     *
     * @param crew 찾는 member 들이 가입한 crew
     * @param role 찾는 member 들의 role
     * @return crew 에 가입한 member 중 role 을 가지는 member 들의 list
     */
    @EntityGraph(attributePaths = {"user"})
    List<Member> findAllByCrewAndRole(Crew crew, MemberRole role);

    /**
     * 특정 RunningNotice 에 참가한 모든 Member 를 반환
     *
     * @param runningNotice Member 들을 확인할 RunningNotice
     * @return 특정 RunningNotice 에 참가한 모든 Member 를 반환
     */
    @Query(value = "select rm.member from RunningMember rm join fetch rm.member.user " +
            "where rm.runningNotice = :runningNotice")
    List<Member> findAllByRunningNotice(@Param("runningNotice") RunningNotice runningNotice);

    /**
     * uesr 의 crew 가입 여부 반환
     *
     * @param user
     * @param crew
     * @return user 가 crew 에 가입했다면 true, 아니라면 false
     */
    boolean existsByUserAndCrew(User user, Crew crew);

    /**
     * 특정 user 와 crew 의 값을 가지는 Member 반환. 없다면 Optional.empty() 반환
     *
     * @param user
     * @param crew
     * @return 특정 user 와 crew 의 값을 가지는 Member. 없다면 Optional.empty()
     */
    Optional<Member> findByUserAndCrew(User user, Crew crew);

    /**
     * crewId 의 리스트에 포함된 id 를 가지는 Crew 들의 멤버수를 반환
     * 
     * @param crewIds Crew 의 id 가 담긴 리스트
     * @return crewId 와 Crew 의 멤버수가 담긴 Object[] 의 리스트. Object[0] 에는 crewId,
     * Object[1] 에는 Crew 의 멤버수
     */
    @Query("select m.crew.id, count(m) from Member m where m.crew.id in (:crewIds) group by m.crew")
    List<Object[]> countAllByCrewIds(@Param("crewIds") List<Long> crewIds);

}
