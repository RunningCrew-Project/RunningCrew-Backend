package com.project.runningcrew.board.entity;

import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update boards set deleted = true where board_id = ?")
@Getter
@DiscriminatorValue("review")
@NoArgsConstructor
public class ReviewBoard extends Board{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_record_id")
    private RunningRecord runningRecord;

    public ReviewBoard(Member member, String title, String content, RunningRecord runningRecord) {
        super(member, title, content);
        this.runningRecord = runningRecord;
    }

    public ReviewBoard(Long id, Member member, String title, String detail, RunningRecord runningRecord) {
        super(id, member, title, detail);
        this.runningRecord = runningRecord;
    }

    public void updateRunningRecord(RunningRecord runningRecord) {
        this.runningRecord = runningRecord;
    }

}
