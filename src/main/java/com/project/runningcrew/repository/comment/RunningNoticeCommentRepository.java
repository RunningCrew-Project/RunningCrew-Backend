package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.comment.RunningNoticeComment;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RunningNoticeCommentRepository extends JpaRepository<RunningNoticeComment, Long> {
    List<RunningNoticeComment> findAllByRunningNotice(RunningNotice runningNotice);


    /**
     * RunningNotice 의 id 정보를 리스트로 입력받아 각 RunningNotice 의 댓글 갯수 정보를 반환한다.
     * @param runningNoticeId RunningNotice Id 리스트 정보
     * @return 각 RunningNotice 의 댓글 수 리스트
     */
    @Query("select count(rc) from RunningNoticeComment rc where rc.runningNotice.id in (:runningNoticeId) group by rc.runningNotice.id")
    List<Integer> countByRunningNoticeId(@Param("runningNoticeId") List<Long> runningNoticeId);

}
