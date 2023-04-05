package com.project.runningcrew.runningrecord.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Getter
@Table(name = "gps")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gps extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gps_id")
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @PositiveOrZero(message = "순서는 0 이상입니다.")
    @Column(nullable = false)
    private int gpsOffset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_record_id", nullable = false)
    private RunningRecord runningRecord;

    public Gps(Double latitude, Double longitude, int gpsOffset, RunningRecord runningRecord) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.gpsOffset = gpsOffset;

        if (runningRecord != null) {
            setRunningRecord(runningRecord);
        }
    }

    public Gps(Long id, Double latitude, Double longitude, int gpsOffset, RunningRecord runningRecord) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gpsOffset = gpsOffset;

        if (runningRecord != null) {
            setRunningRecord(runningRecord);
        }
    }

    private void setRunningRecord(RunningRecord runningRecord) {
        this.runningRecord = runningRecord;
        runningRecord.getGpsList().add(this);
    }

}
