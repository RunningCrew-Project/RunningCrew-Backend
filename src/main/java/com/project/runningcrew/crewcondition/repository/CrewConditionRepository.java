package com.project.runningcrew.crewcondition.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewConditionRepository extends JpaRepository<CrewCondition, Long> {

    /**
     * 특정 crew 의 CrewCondition 읇 반환. 없다면 Optional.empty()
     *
     * @param crew
     * @return crew 에 해당하는 CrewCondition. 없다면 Optional.empty()
     */
    Optional<CrewCondition> findByCrew(Crew crew);

    /**
     * 특정 crew 의 CrewCondition 삭제
     * @param crew
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from CrewCondition c where c.crew = :crew")
    void deleteByCrew(@Param("crew") Crew crew);

}
