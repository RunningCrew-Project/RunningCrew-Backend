package com.project.runningcrew.runningrecord.entity;

import com.project.runningcrew.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SQLDelete(sql = "update running_records set deleted = true where running_record_id = ?")
@Getter
@DiscriminatorValue("personal")
@NoArgsConstructor
public class PersonalRunningRecord extends RunningRecord{

    @Builder
    public PersonalRunningRecord(Long id, String title, LocalDateTime startDateTime, String location,
                                 double runningDistance, int runningTime, int runningFace,
                                 int calories, String running_detail, User user) {
        super(id, title, startDateTime, location, runningDistance, runningTime, runningFace,
                calories, running_detail, user);
    }

}
