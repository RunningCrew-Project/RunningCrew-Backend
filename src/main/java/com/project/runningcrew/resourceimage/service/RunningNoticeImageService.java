package com.project.runningcrew.resourceimage.service;

import com.project.runningcrew.exception.notFound.ImageNotFoundException;
import com.project.runningcrew.resourceimage.entity.RunningNoticeImage;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import com.project.runningcrew.resourceimage.repository.RunningNoticeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningNoticeImageService {

    private final RunningNoticeImageRepository runningNoticeImageRepository;
    private String defaultImageUrl = "defaultImageUrl";

    /**
     * 입력받은 id 를 가진 RunningNoticeImage 를 찾아 반환한다. 없다면 ImageNotFoundException 을 throw 한다.
     *
     * @param runningNoticeImageId 찾는 RunningNoticeImage 의 id
     * @return runningNoticeImageId 를 id 로 가지는 RunningNoticeImage
     * @throws ImageNotFoundException runningNoticeImageId 에 해당하는 RunningNoticeImage 가 존재하지 않을 때
     */
    public RunningNoticeImage findById(Long runningNoticeImageId) {
        return runningNoticeImageRepository.findById(runningNoticeImageId).orElseThrow(ImageNotFoundException::new);
    }

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
     * @return runningNoticeId 와 runningNoticeId 에 포함된 RunningNoticeImage 의 Map.
     * 포함된 RunningNoticeImage 가 없다면 defaultImageUrl 을 가진 RunningNoticeImage
     */
    public Map<Long, RunningNoticeImage> findFirstImages(List<Long> runningNoticeIds) {
        Map<Long, RunningNoticeImage> maps = new HashMap<>();
        List<RunningNoticeImage> images = runningNoticeImageRepository
                .findImagesByRunningNoticeIds(runningNoticeIds);
        for (Long runningNoticeId : runningNoticeIds) {
            RunningNoticeImage first = images.stream()
                    .filter(image -> image.getRunningNotice().getId().equals(runningNoticeId))
                    .findFirst().orElseGet(() -> new RunningNoticeImage(defaultImageUrl, null));
            maps.put(runningNoticeId, first);
        }
        return maps;
    }

    /**
     * runningNoticeId 의 리스트를 받아, runningNoticeId 와 RunningNoticeImage url 의 Map 을 반환한다.
     * runningNoticeId 에 포함된 RunningNoticeImage 가 있다면 RunningNoticeImage 중 하나의 url 을 가지고,
     * runningNoticeId 에 포함된 RunningNoticeImage 가 없다면 "" 를 가진다.
     *
     * @param runningNoticeIds Board 의 id 를 가진 리스트
     * @return runningNoticeId 와 runningNoticeId 에 포함된 RunningNoticeImage 의 Map.
     * 포함된 RunningNoticeImage 가 없다면 defaultImageUrl 을 가진 RunningNoticeImage
     */
    public Map<Long, String> findFirstImageUrls(List<Long> runningNoticeIds) {
        Map<Long, String> maps = new HashMap<>();
        List<RunningNoticeImage> images = runningNoticeImageRepository
                .findImagesByRunningNoticeIds(runningNoticeIds);
        for (Long runningNoticeId : runningNoticeIds) {
            Optional<RunningNoticeImage> optionalFirst = images.stream()
                    .filter(image -> image.getRunningNotice().getId().equals(runningNoticeId))
                    .findFirst();
            maps.put(runningNoticeId, optionalFirst.isPresent() ? optionalFirst.get().getFileName() : "");
        }
        return maps;
    }

}
