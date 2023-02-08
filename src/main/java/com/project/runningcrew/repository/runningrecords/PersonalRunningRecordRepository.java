package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonalRunningRecordRepository extends JpaRepository<PersonalRunningRecord, Long> {



    List<PersonalRunningRecord> findAllByUser(User user);

}
