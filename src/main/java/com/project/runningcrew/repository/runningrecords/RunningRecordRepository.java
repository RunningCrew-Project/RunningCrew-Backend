package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunningRecordRepository extends JpaRepository<RunningRecord, Long> {

    Slice<RunningRecord> findByUser(User user, Pageable pageable);

    @Query("select  r from RunningRecord r " +
            "where r.startDateTime >= :today and r.startDateTime <:tomorrow")
    List<RunningRecord> findAllByStartDate(@Param("today") LocalDateTime today,
                                                   @Param("tomorrow") LocalDateTime tomorrow);

}
