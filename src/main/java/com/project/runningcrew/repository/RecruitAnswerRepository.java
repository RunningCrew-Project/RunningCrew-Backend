package com.project.runningcrew.repository;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RecruitAnswer;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitAnswerRepository extends JpaRepository<RecruitAnswer, Long> {

    /**
     * 특정 User 가 작성한 모집 양식 답변을 offset 순으로 정렬하여 반환한다.
     * @param user
     * @param crew
     * @return list of RecruitAnswer
     */
    @Query("select ra from RecruitAnswer ra where ra.user = :user and ra.crew = :crew order by ra.answerOffset")
    List<RecruitAnswer> findAllByUserAndCrew(@Param("user") User user, @Param("crew") Crew crew);


    /**
     * 특정 Crew 의 RecruitAnswer 을 작성한 User 반환
     * @param crew
     * @return
     */
    @Query("select distinct(ra.user) from RecruitAnswer ra where ra.crew = :crew")
    List<User> findUserByRecruitAnswer(@Param("crew") Crew crew);

}
