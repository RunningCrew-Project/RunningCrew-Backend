package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
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

}
