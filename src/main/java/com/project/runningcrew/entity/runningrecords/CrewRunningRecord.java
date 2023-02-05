package com.project.runningcrew.entity.runningrecords;

import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.members.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue("crew")
@NoArgsConstructor
public class CrewRunningRecord extends RunningRecord{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_notice_id")
    private RunningNotice runningNotice;

    public CrewRunningRecord(double runningDistance, int runningTime, int runningFace, int calories, String running_detail,
                             Member member, RunningNotice runningNotice) {
        super(runningDistance, runningTime, runningFace, calories, running_detail);
        this.member = member;
        this.runningNotice = runningNotice;
    }

}
