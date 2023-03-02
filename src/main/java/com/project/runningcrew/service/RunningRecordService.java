package com.project.runningcrew.service;

import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.notFound.RunningRecordNotFoundException;
import com.project.runningcrew.repository.images.RunningRecordImageRepository;
import com.project.runningcrew.repository.runningrecords.RunningRecordRepository;
import com.project.runningcrew.service.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunningRecordService {

    private final RunningRecordRepository runningRecordRepository;
    private final ImageService imageService;
    private final RunningRecordImageRepository runningRecordImageRepository;
    private final String imageDirName = "runningRecord";

    /**
     * 입력받은 id 를 가진 RunningRecord 를 찾아 반환한다. 없다면 RunningRecordNotFoundException 를 throw 한다.
     *
     * @param runningRecordId 찾는 RunningRecord 의 id
     * @return 입력받은 id 를 가진 RunningRecord 를 반환한다.
     * @throws RunningRecordNotFoundException 입력 받은 id 를 가진 RunningRecord 가 없을때
     */
    public RunningRecord findById(Long runningRecordId) {
        return runningRecordRepository.findById(runningRecordId)
                .orElseThrow(RunningRecordNotFoundException::new);
    }

    /**
     * 입력받은 MultipartFile 들과 RunningRecord 를 저장하고, RunningRecord 에 부여된 id 를 반환한다.
     *
     * @param runningRecord  저장할 RunningRecord
     * @param multipartFiles 저장할 모든 MultipartFile
     * @return RunningRecord 에 부여된 id
     */
    @Transactional
    public Long saveRunningRecord(RunningRecord runningRecord, List<MultipartFile> multipartFiles) {
        RunningRecord savedRunningRecord = runningRecordRepository.save(runningRecord);
        for (MultipartFile multipartFile : multipartFiles) {
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            runningRecordImageRepository.save(new RunningRecordImage(imageUrl, savedRunningRecord));
        }
        return savedRunningRecord.getId();
    }

    /**
     * 입력받은 User 의 RunningRecord 의 Slice 를 반환
     *
     * @param user     찾는 RunningRecord 의 User
     * @param pageable
     * @return RunningRecord 의 Slice
     */
    public Slice<RunningRecord> findByUser(User user, Pageable pageable) {
        return runningRecordRepository.findByUser(user, pageable);
    }

    /**
     * 입력받은 User 가 LocalDate 에 런닝을 시작한 모든 RunningRecord 를 반환
     *
     * @param user      찾는 RunningRecord 의 User
     * @param localDate 런닝을 시작한 날
     * @return 런닝을 시작한 날이 LocalDate 인 User 의 모든 RunningRecord
     */
    public List<RunningRecord> findAllByUserAndStartDate(User user, LocalDate localDate) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        LocalDateTime nextDateTime = dateTime.plusDays(1);
        return runningRecordRepository.findAllByUserAndStartDateTimes(user, dateTime, nextDateTime);
    }

    /**
     * 특정 user 의 모든 RunningRecord 를 삭제한다.
     *
     * @param user RunningRecord 를 삭제할 user
     */
    @Transactional
    public void deleteAllByUser(User user) {
        runningRecordRepository.deleteAllByUser(user);
    }


    /**
     * 입력받은 user 가 특정 년도 특정 월에 시행한 모든 RunningRecord 의 런닝 거리의 합
     * 
     * @param user
     * @param year 찾는 년도
     * @param month 찾는 월
     * @return  user 가 특정 년도 특정 월에 시행한 모든 RunningRecord 의 런닝 거리의 합
     */
    public Double getSumOfRunningDistanceOfMonth(User user, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);

        return runningRecordRepository.getSumOfRunningDistanceByUserAndStartDateTimes(user, start, end);
    }

    /**
     * 입력받은 user 가 특정 년도 특정 월에 시행한 모든 RunningRecord 의 런닝 시간의 합
     *
     * @param user
     * @param year 찾는 년도
     * @param month 찾는 월
     * @return  user 가 특정 년도 특정 월에 시행한 모든 RunningRecord 의 런닝 시간의 합
     */
    public Integer getSumOfRunningTimeOfMonth(User user, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);

        return runningRecordRepository.getSumOfRunningTimeByUserAndStartDateTimes(user, start, end);
    }

}
