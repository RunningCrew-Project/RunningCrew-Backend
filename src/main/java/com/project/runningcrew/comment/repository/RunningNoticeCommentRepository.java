package com.project.runningcrew.comment.repository;

import com.project.runningcrew.comment.entity.RunningNoticeComment;
import com.project.runningcrew.common.dto.SimpleCommentDto;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RunningNoticeCommentRepository extends JpaRepository<RunningNoticeComment, Long> {


    /**
     * 입력받은 RunningNotice 의 댓글 리스트를 반환한다.
     * @param runningNotice 입력받은 RunningNotice
     * @return 입력받은 RunningNotice 의 댓글 리스트
     */
    @Query("select new com.project.runningcrew.common.dto.SimpleCommentDto(rc.id, rc.createdDate, rc.detail, u.nickname, u.imgUrl) " +
            "from RunningNoticeComment rc " +
            "inner join RunningNotice r on r = rc.runningNotice " +
            "inner join Member m on m = rc.member " +
            "inner join User u on m.user = u " +
            "where rc.runningNotice = :runningNotice")
    List<SimpleCommentDto> findAllByRunningNotice(@Param("runningNotice") RunningNotice runningNotice);


    /**
     * 입력받은 runningNotice 에 작성된 댓글을 모두 삭제한다.
     * @param runningNotice 댓글을 모두 삭제할 runningNotice
     */
    @Modifying
    @Query("delete from RunningNoticeComment rc where rc.runningNotice = :runningNotice")
    void deleteCommentAtRunningNotice(@Param("runningNotice") RunningNotice runningNotice);


    /**
     * RunningNotice 의 id 정보를 리스트로 입력받아 각 RunningNotice 의 댓글 갯수 정보를 반환한다.
     * @param runningNoticeIds RunningNotice 의 IdList 정보
     * @return 각 RunningNotice 의 댓글 수 리스트
     */
    @Query("select rc.runningNotice.id, count(rc) from RunningNoticeComment rc where rc.runningNotice.id in (:runningNoticeIds) group by rc.runningNotice.id")
    List<Object[]> countAllByRunningNoticeIds(@Param("runningNoticeIds") List<Long> runningNoticeIds);










    /**
     *
     * 미사용 예정, Test 코드 컴파일 오류때문에 남겨둠.
     * 미사용 예정, Test 코드 컴파일 오류때문에 남겨둠.
     * 미사용 예정, Test 코드 컴파일 오류때문에 남겨둠.
     *
     */
    @Query("select count(rc) from RunningNoticeComment rc where rc.runningNotice.id in (:runningNoticeId) group by rc.runningNotice.id")
    List<Integer> countByRunningNoticeId(@Param("runningNoticeId") List<Long> runningNoticeId);




}
