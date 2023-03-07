package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonalRunningRecordRepository extends JpaRepository<PersonalRunningRecord, Long> {

}
