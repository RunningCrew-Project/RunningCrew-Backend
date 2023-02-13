package com.project.runningcrew.repository;

import com.project.runningcrew.entity.RecruitAnswer;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitAnswerRepository extends JpaRepository<RecruitAnswer, Long> {

    /**
     * @param user
     * @return list of RecruitAnswer
     * 특정 User 가 작성한 모집 양식 답변을 offset 순으로 정렬하여 반환한다.
     */
    @Query("select ra from RecruitAnswer ra where ra.user = :user order by ra.answerOffset")
    List<RecruitAnswer> findAllByUserId(@Param("user") User user);

}
