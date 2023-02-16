package com.project.runningcrew.service;

import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.RunningRecordNotFoundException;
import com.project.runningcrew.repository.runningrecords.RunningRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
     * 입력받은 id 를 가진 RunningRecord 를 찾아 반환한다. 없다면 RunningRecordNotFoundException 를 throw 한다.
     * @param runningRecordId 찾는 RunningRecord 의 id
     * @return 입력받은 id 를 가진 RunningRecord 를 반환한다.
     * @throws RunningRecordNotFoundException 입력 받은 id 를 가진 RunningRecord 가 없을때
     */
    public RunningRecord findById(Long runningRecordId) {
        return runningRecordRepository.findById(runningRecordId)
                .orElseThrow(RunningRecordNotFoundException::new);
    }

    /**
     * 입력받은 RunningRecord 를 저장하고, RunningRecord 에 부여된 id 를 반환한다.
     * @param runningRecord 저장할 RunningRecord
     * @return RunningRecord 에 부여된 id
     */
    @Transactional
    public Long saveRunningRecord(RunningRecord runningRecord) {
        return runningRecordRepository.save(runningRecord).getId();
    }


    /**
     * 입력받은 User 의 RunningRecord 의 Slice
     * @param user 찾는 RunningRecord 의 User
     * @param pageable
     * @return RunningRecord 의 Slice
     */
    public Slice<RunningRecord> findByUser(User user, Pageable pageable) {
        return runningRecordRepository.findByUser(user, pageable);
    }

    /**
     * 입력받은 User 가 LocalDate 에 런닝을 시작한 모든 RunningRecord 를 반환
     * @param user 찾는 RunningRecord 의 User
     * @param localDate 런닝을 시작한 날
     * @return 런닝을 시작한 날이 LocalDate 인 User 의 모든 RunningRecord
     */
    public List<RunningRecord> findAllByUserAndStartDate(User user, LocalDate localDate) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        LocalDateTime nextDateTime = dateTime.plusDays(1);
        return runningRecordRepository.findAllByUserAndStartDateTimes(user, dateTime, nextDateTime);
    }

}
