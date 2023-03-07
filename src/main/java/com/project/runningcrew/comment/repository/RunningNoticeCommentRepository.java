package com.project.runningcrew.comment.repository;

import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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


    /**
     * 입력받은 runningNotice 에 작성된 댓글을 모두 삭제한다.
     * @param runningNotice 댓글을 모두 삭제할 runningNotice
     */
    @Modifying
    @Query("delete from RunningNoticeComment rc where rc.runningNotice = :runningNotice")
    void deleteCommentAtRunningNotice(@Param("runningNotice") RunningNotice runningNotice);


}
