package com.project.runningcrew.entity.runningrecords;

import com.project.runningcrew.entity.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("personal")
@NoArgsConstructor
public class PersonalRunningRecord extends RunningRecord{

    @Builder
    public PersonalRunningRecord(Long id, LocalDateTime startDateTime, double runningDistance,
                                 int runningTime, int runningFace, int calories,
                                 String running_detail, User user) {
        super(id, startDateTime, runningDistance, runningTime, runningFace, calories, running_detail, user);
    }

}
