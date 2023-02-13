package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.runningrecords.CrewRunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CrewRunningRecordRepository extends JpaRepository<CrewRunningRecord, Long> {

}
