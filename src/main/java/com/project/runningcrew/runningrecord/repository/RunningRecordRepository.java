package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunningRecordRepository extends JpaRepository<RunningRecord, Long> {

    /**
     * 특정 user 의 RunningRecord 들을 페이징 하여 반환
     *
     * @param user     찾는 RunningRecord 를 가지는 User
     * @param pageable
     * @return 페이징 조건에 맞는 user 의 RunningRecord 가 담긴 Slice
     */
    Slice<RunningRecord> findByUser(User user, Pageable pageable);

    /**
     * 특정 user 의 모든 RunningRecord 반환
     *
     * @param user 찾는 RunningRecord 를 가지는 User
     * @return 특정 user 의 모든 RunningRecord
     */
    List<RunningRecord> findAllByUser(User user);

    /**
     * 특정 user 의 RunningRecord 중 범위에 속하는 날짜에 런닝을 시작한 모든 RunningRecord 를 반환
     *
     * @param user  찾는 user
     * @param start 범위 시작
     * @param end   범위 종료. end 는 포함하지 않음
     * @return 특정 user 의 start 와 end 사이에 런닝을 시작한 모든 RunningRecord 들의 list
     */
    @Query("select r from RunningRecord r where r.user = :user and " +
            "r.startDateTime >= :start and r.startDateTime <:end")
    List<RunningRecord> findAllByUserAndStartDateTimes(@Param("user") User user,
                                                       @Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);


    /**
     * 특정 user 의 모든 RunningRecord 를 삭제한다.
     *
     * @param user RunningRecord 를 삭제할 user
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update RunningRecord r set r.deleted = true where r.user = :user")
    void deleteAllByUser(@Param("user") User user);


    /**
     * 특정 user 의 RunningRecord 중 범위에 속하는 날짜에 런닝을 시작한 모든 RunningRecord 의
     * 런닝 거리의 합을 반환
     *
     * @param user  찾는 user
     * @param start 범위 시작
     * @param end   범위 종료. end 는 포함하지 않음
     * @return 특정 user 의 start 와 end 사이에 런닝을 시작한 모든 RunningRecord 들의 런닝 거리의 합
     */
    @Query("select sum(r.runningDistance) from RunningRecord r where r.user = :user and " +
            "r.startDateTime >= :start and r.startDateTime <:end")
    Double getSumOfRunningDistanceByUserAndStartDateTimes(@Param("user") User user,
                                                           @Param("start") LocalDateTime start,
                                                           @Param("end") LocalDateTime end);

    /**
     * 특정 user 의 RunningRecord 중 범위에 속하는 날짜에 런닝을 시작한 모든 RunningRecord 의
     * 런닝 시간의 합을 반환
     *
     * @param user  찾는 user
     * @param start 범위 시작
     * @param end   범위 종료. end 는 포함하지 않음
     * @return 특정 user 의 start 와 end 사이에 런닝을 시작한 모든 RunningRecord 들의 런닝 시간의 합
     */
    @Query("select sum(r.runningTime) from RunningRecord r where r.user = :user and " +
            "r.startDateTime >= :start and r.startDateTime <:end")
    Integer getSumOfRunningTimeByUserAndStartDateTimes(@Param("user") User user,
                                                       @Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);

}
