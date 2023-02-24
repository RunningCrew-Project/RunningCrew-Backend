package com.project.runningcrew.entity.runningrecords;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("crew")
@NoArgsConstructor
public class CrewRunningRecord extends RunningRecord {

    @Builder
    public CrewRunningRecord(Long id, String title, LocalDateTime startDateTime, String location,
                             double runningDistance, int runningTime, int runningFace,
                             int calories, String running_detail, User user) {
        super(id, title, startDateTime, location, runningDistance, runningTime, runningFace,
                calories, running_detail, user);
    }

}
