package com.project.runningcrew.resourceimage.entity;

import com.project.runningcrew.runningrecord.entity.RunningRecord;
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

    public RunningRecordImage(Long id, String fileName, RunningRecord runningRecord) {
        super(id, fileName);
        this.runningRecord = runningRecord;
    }

}
