package com.project.runningcrew.resourceimage.service;

import com.project.runningcrew.resourceimage.entity.RunningRecordImage;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.resourceimage.repository.RunningRecordImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningRecordImageService {

    private final RunningRecordImageRepository runningRecordImageRepository;
    private final String defaultImageUrl = "defaultImageUrl";

    /**
     * RunningRecord 에 포함된 모든 RunningRecordImage 를 반환한다.
     *
     * @param board
     * @return RunningRecord 에 포함된 모든 RunningRecordImage
     */
    public List<RunningRecordImage> findAllByRunningRecord(RunningRecord board) {
        return runningRecordImageRepository.findAllByRunningRecord(board);
    }

    /**
     * runningRecordId 의 리스트를 받아, runningRecordId 와 RunningNoticeImage 의 Map 을 반환한다.
     * runningRecordId 에 포함된 RunningRecordImage 가 있다면 RunningRecordImage 중 하나를 가지고,
     * runningRecordId 에 포함된 RunningRecordImage 가 없다면 defaultImageUrl 을 가진 RunningRecordImage 를 가진다.
     *
     * @param runningRecordIds Board 의 id 를 가진 리스트
     * @return runningRecordId 와 runningRecordId 에 포함된 BoardImage 의 Map.
     * 포함된 RunningRecordImage 가 없다면 defaultImageUrl 을 가진 RunningRecordImage
     */
    public Map<Long, RunningRecordImage> findFirstImages(List<Long> runningRecordIds) {
        Map<Long, RunningRecordImage> maps = new HashMap<>();
        List<RunningRecordImage> images = runningRecordImageRepository
                .findImagesByRunningRecordIds(runningRecordIds);
        for (Long runningRecordId : runningRecordIds) {
            RunningRecordImage first = images.stream()
                    .filter(image -> image.getRunningRecord().getId() == runningRecordId)
                    .findFirst().orElseGet(() -> new RunningRecordImage(defaultImageUrl, null));
            maps.put(runningRecordId, first);
        }
        return maps;
    }

}
