package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningNoticeImageRepository extends JpaRepository<RunningNoticeImage, Long> {

    List<RunningNoticeImage> findAllByRunningNotice(RunningNotice runningNotice);

}
