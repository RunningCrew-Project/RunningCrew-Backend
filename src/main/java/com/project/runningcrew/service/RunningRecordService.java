package com.project.runningcrew.service;

import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.RunningRecordNotFoundException;
import com.project.runningcrew.repository.runningrecords.RunningRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningRecordService {

    private final RunningRecordRepository runningRecordRepository;

    /**
     *
     * @param runningRecordId
     * @return
     */
    public RunningRecord findById(Long runningRecordId) {
        return runningRecordRepository.findById(runningRecordId)
                .orElseThrow(RunningRecordNotFoundException::new);
    }

    /**
     *
     * @param runningRecord
     * @return
     */
    @Transactional
    public RunningRecord saveRunningRecord(RunningRecord runningRecord) {
        return runningRecordRepository.save(runningRecord);
    }


    /**
     *
     * @param user
     * @param pageable
     * @return
     */
    public Slice<RunningRecord> findByUser(User user, Pageable pageable) {
        return runningRecordRepository.findByUser(user, pageable);
    }

    /**
     *
     * @param localDate
     * @return
     */
    public List<RunningRecord> getRunningRecordsByDate(User user, LocalDate localDate) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        LocalDateTime nextDateTime = dateTime.plusDays(1);
        return runningRecordRepository.findAllByUserAndStartDateTimes(user, dateTime, nextDateTime);
    }

}
