package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalRunningRecordRepository extends JpaRepository<PersonalRunningRecord, Long> {

}
