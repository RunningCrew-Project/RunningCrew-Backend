package com.project.runningcrew.resourceimage.entity;

import com.project.runningcrew.runningrecord.entity.RunningRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "update images set deleted = true where image_id = ?")
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
