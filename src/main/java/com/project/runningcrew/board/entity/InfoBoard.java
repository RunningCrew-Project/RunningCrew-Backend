package com.project.runningcrew.board.entity;

import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@DiscriminatorValue("info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InfoBoard extends Board {

    @OneToOne
    @JoinColumn(name = "running_record_id")
    private RunningRecord runningRecord;

    public InfoBoard(Member member, String title, String detail, RunningRecord runningRecord) {
        super(member, title, detail);
        this.runningRecord = runningRecord;
    }

    public void updateRunningRecord(RunningRecord runningRecord) {
        this.runningRecord = runningRecord;
    }

}
