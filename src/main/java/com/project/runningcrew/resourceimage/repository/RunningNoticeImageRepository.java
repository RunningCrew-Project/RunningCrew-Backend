package com.project.runningcrew.resourceimage.repository;

import com.project.runningcrew.resourceimage.entity.RunningNoticeImage;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningNoticeImageRepository extends JpaRepository<RunningNoticeImage, Long> {

    /**
     * runningNotice에 포함된 모든 RunningNoticeImage 반환
     *
     * @param runningNotice
     * @return runningNotice 에 포함된 모든 RunningNoticeImage 의 list
     */
    List<RunningNoticeImage> findAllByRunningNotice(RunningNotice runningNotice);

    /**
     * runningNotice에 포함된 모든 RunningNoticeImage 삭제
     *
     * @param runningNotice
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RunningNoticeImage r where r.runningNotice = :runningNotice")
    void deleteAllByRunningNotice(@Param("runningNotice") RunningNotice runningNotice);

    /**
     * runningNoticeId 의 리스트에 포함된 runningNoticeId 를 가진 RunningRecordImage 반환
     *
     * @param runningNoticeIds RunningRecord 의 id 를 가진 리스트
     * @return RunningNotice 의 id 가 runningNoticeIds 에 포함된 모든 RunningNoticeImage.
     */
    @Query("select r from RunningNoticeImage r where r.runningNotice.id in (:runningNoticeIds)")
    List<RunningNoticeImage> findImagesByRunningNoticeIds(@Param("runningNoticeIds") List<Long> runningNoticeIds);

}
