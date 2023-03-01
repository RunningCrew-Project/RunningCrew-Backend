package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningNoticeImageService {

    private final RunningNoticeImageRepository runningNoticeImageRepository;

    /**
     * RunningNotice 에 포함된 모든 RunningNoticeImage 를 반환한다.
     *
     * @param board
     * @return RunningNoticeImage 에 포함된 모든 RunningNoticeImage
     */
    public List<RunningNoticeImage> findAllByRunningNotice(RunningNotice board) {
        return runningNoticeImageRepository.findAllByRunningNotice(board);
    }

}
