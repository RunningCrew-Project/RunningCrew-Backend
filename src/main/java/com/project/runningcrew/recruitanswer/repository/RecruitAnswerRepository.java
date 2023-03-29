package com.project.runningcrew.recruitanswer.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.recruitanswer.entity.RecruitAnswer;
import com.project.runningcrew.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

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
     * 특정 user 가 작성한 RecruitAnswer 를 삭제한다.
     * @param user
     * @param crew
     */
    @Modifying
    @Query("delete from RecruitAnswer ra where ra.user = :user and ra.crew = :crew")
    void deleteByUserAndCrew(@Param("user") User user, @Param("crew") Crew crew);


    /**
     * 특정 Crew 의 RecruitAnswer 을 작성한 User 반환
     * @param crew
     * @return
     */
    @Query("select distinct(ra.user) from RecruitAnswer ra where ra.crew = :crew")
    List<User> findUserByCrew(@Param("crew") Crew crew);


    /**
     * 특정 crew 의 recruitAnswer 를 모두 삭제한다.
     * @param crew
     */
    @Modifying
    @Query("delete from RecruitAnswer ra where ra.crew = :crew")
    void deleteAllByCrew(@Param("crew") Crew crew);


    /**
     * 특정 user 가 작성한 recruitAnswer 를 모두 삭제한다.
     * @param user
     */
    @Modifying
    @Query("delete from RecruitAnswer ra where ra.user = :user")
    void deleteAllByUser(@Param("user") User user);


}
