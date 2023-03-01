package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningNoticeImageService {

    private final RunningNoticeImageRepository runningNoticeImageRepository;
    private final String defaultImageUrl = "defaultImageUrl";

    /**
     * RunningNotice 에 포함된 모든 RunningNoticeImage 를 반환한다.
     *
     * @param board
     * @return RunningNoticeImage 에 포함된 모든 RunningNoticeImage
     */
    public List<RunningNoticeImage> findAllByRunningNotice(RunningNotice board) {
        return runningNoticeImageRepository.findAllByRunningNotice(board);
    }

    /**
     * runningNoticeId 의 리스트를 받아, runningNoticeId 와 RunningNoticeImage 의 Map 을 반환한다.
     * runningNoticeId 에 포함된 RunningNoticeImage 가 있다면 RunningNoticeImage 중 하나를 가지고,
     * runningNoticeId 에 포함된 RunningNoticeImage 가 없다면 defaultImageUrl 을 가진 RunningNoticeImage 를 가진다.
     *
     * @param runningNoticeIds Board 의 id 를 가진 리스트
     * @return runningNoticeId 와 runningNoticeId 에 포함된 BoardImage 의 Map. 포함된 BoardImage 가 없다면 null 이 포함됨
     */
    public Map<Long, RunningNoticeImage> findFirstImages(List<Long> runningNoticeIds) {
        Map<Long, RunningNoticeImage> maps = new HashMap<>();
        List<RunningNoticeImage> images = runningNoticeImageRepository
                .findImagesByRunningNoticeIds(runningNoticeIds);
        for (Long runningNoticeId : runningNoticeIds) {
            RunningNoticeImage first = images.stream()
                    .filter(image -> image.getRunningNotice().getId() == runningNoticeId)
                    .findFirst().orElseGet(() -> new RunningNoticeImage(defaultImageUrl, null));
            maps.put(runningNoticeId, first);
        }
        return maps;
    }

}
