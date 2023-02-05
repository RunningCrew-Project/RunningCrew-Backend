package com.project.runningcrew.entity.runningrecords;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.Gps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "running_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RunningRecord extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "running_record_id")
    private Long id;

    private double runningDistance;

    private int runningTime;

    private int runningFace;

    private int calories;

    private String running_detail;

    @OneToMany(mappedBy = "runningRecord", cascade = CascadeType.REMOVE)
    private List<Gps> gpsList = new ArrayList<>();

    protected RunningRecord(double runningDistance, int runningTime, int runningFace, int calories, String running_detail) {
        this.runningDistance = runningDistance;
        this.runningTime = runningTime;
        this.runningFace = runningFace;
        this.calories = calories;
        this.running_detail = running_detail;
    }

}
