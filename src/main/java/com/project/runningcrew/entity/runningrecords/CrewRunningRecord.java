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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_notice_id")
    private RunningNotice runningNotice;

    @Builder
    public CrewRunningRecord(LocalDateTime startDateTime, double runningDistance, int runningTime,
                             int runningFace, int calories, String running_detail, User user,
                             Crew crew, RunningNotice runningNotice) {
        super(startDateTime, runningDistance, runningTime, runningFace, calories, running_detail, user);
        this.crew = crew;
        this.runningNotice = runningNotice;
    }

}
