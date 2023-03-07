package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.runningrecord.entity.CrewRunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CrewRunningRecordRepository extends JpaRepository<CrewRunningRecord, Long> {

}
