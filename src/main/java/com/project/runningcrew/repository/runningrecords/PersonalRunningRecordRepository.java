package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonalRunningRecordRepository extends JpaRepository<PersonalRunningRecord, Long> {

}
