package com.project.runningcrew.entity.runningrecords;

import com.project.runningcrew.entity.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("personal")
@NoArgsConstructor
public class PersonalRunningRecord extends RunningRecord{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PersonalRunningRecord(LocalDateTime startDateTime, double runningDistance, int runningTime,
                                 int runningFace, int calories, String running_detail, User user) {
        super(startDateTime, runningDistance, runningTime, runningFace, calories, running_detail);
        this.user = user;
    }
}
