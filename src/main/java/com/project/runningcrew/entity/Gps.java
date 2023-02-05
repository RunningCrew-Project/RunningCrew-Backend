package com.project.runningcrew.entity;

import com.project.runningcrew.entity.runningrecords.RunningRecord;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gps extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    private Double latitude;

    private Double longitude;

    private int offset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_record_id")
    private RunningRecord runningRecord;

    public Gps(Double latitude, Double longitude, int offset, RunningRecord runningRecord) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.offset = offset;

        if (runningRecord != null) {
            setRunningRecord(runningRecord);
        }
    }

    private void setRunningRecord(RunningRecord runningRecord) {
        this.runningRecord = runningRecord;
        runningRecord.getGpsList().add(this);
    }

}
