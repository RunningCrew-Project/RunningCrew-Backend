package com.project.runningcrew.recruitquestion.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.recruitquestion.entity.RecruitQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitQuestionRepository extends JpaRepository<RecruitQuestion, Long> {

    /**
     * @param crew 크루 정보
     * @return list of RecruitQuestion
     * 특정 Crew 의 가입 질문을 offset 순으로 정렬하여 반환한다.
     * SOFT DELETE 적용
     */
    @Query("select rq " +
            "from RecruitQuestion rq " +
            "inner join Crew c on rq.crew = :crew and c.deleted = false " +
            "order by rq.questionOffset")
    List<RecruitQuestion> findAllByCrew(@Param("crew") Crew crew);


    /**
     * 특정 crew 에 저장된 recruitQuestion 을 모두 삭제한다.
     * @param crew 크루 정보
     * SOFT DELETE 적용
     */
    @Modifying
    @Query("update RecruitQuestion rq " +
            "set rq.deleted = true " +
            "where rq.crew = :crew")
    void deleteAllByCrew(@Param("crew") Crew crew);

}
