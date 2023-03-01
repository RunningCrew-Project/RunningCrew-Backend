package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.repository.images.RunningRecordImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningRecordImageService {

    private final RunningRecordImageRepository runningRecordImageRepository;

    /**
     * RunningRecord 에 포함된 모든 RunningRecordImage 를 반환한다.
     *
     * @param board
     * @return RunningRecord 에 포함된 모든 RunningRecordImage
     */
    public List<RunningRecordImage> findAllByRunningRecord(RunningRecord board) {
        return runningRecordImageRepository.findAllByRunningRecord(board);
    }

}
