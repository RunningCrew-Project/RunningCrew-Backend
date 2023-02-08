package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.runningrecords.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningRecordImageRepository extends JpaRepository<RunningRecordImageRepository, Long> {

    List<RunningRecordImageRepository> findAllByRunningRecord(RunningRecord runningRecord);

}
