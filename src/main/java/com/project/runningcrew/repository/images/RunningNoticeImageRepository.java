package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningNoticeImageRepository extends JpaRepository<RunningNoticeImage, Long> {

    /**
     * runningNotice에 포함된 모든 RunningNoticeImage 반환
     * @param runningNotice
     * @return runningNotice 에 포함된 모든 RunningNoticeImage 의 list
     */
    List<RunningNoticeImage> findAllByRunningNotice(RunningNotice runningNotice);

}
