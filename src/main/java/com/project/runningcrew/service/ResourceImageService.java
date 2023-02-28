package com.project.runningcrew.service;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.images.BoardImage;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.repository.images.BoardImageRepository;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
import com.project.runningcrew.repository.images.RunningRecordImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceImageService {

    private final BoardImageRepository boardImageRepository;
    private final RunningNoticeImageRepository runningNoticeImageRepository;
    private final RunningRecordImageRepository runningRecordImageRepository;

    /**
     * Board 에 포함된 모든 BoardImage 를 반환한다.
     *
     * @param board
     * @return Board 에 포함된 모든 BoardImage
     */
    public List<BoardImage> findAllByBoard(Board board) {
        return boardImageRepository.findAllByBoard(board);
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
     * RunningRecord 에 포함된 모든 RunningRecordImage 를 반환한다.
     *
     * @param board
     * @return RunningRecord 에 포함된 모든 RunningRecordImage
     */
    public List<RunningRecordImage> findAllByRunningRecord(RunningRecord board) {
        return runningRecordImageRepository.findAllByRunningRecord(board);
    }

}
