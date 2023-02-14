package com.project.runningcrew.repository.comment;

import com.project.runningcrew.entity.comment.RunningNoticeComment;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningNoticeCommentRepository extends JpaRepository<RunningNoticeComment, Long> {
    List<RunningNoticeComment> findAllByRunningNotice(RunningNotice runningNotice);

}
