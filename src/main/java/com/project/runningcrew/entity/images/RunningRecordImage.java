package com.project.runningcrew.entity.images;

import com.project.runningcrew.entity.boards.Board;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue("running_record")
@NoArgsConstructor
public class RunningRecordImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_record_id")
    private RunningRecord runningRecord;

    public RunningRecordImage(String fileName, RunningRecord runningRecord) {
        super(fileName);
        this.runningRecord = runningRecord;
    }

}
