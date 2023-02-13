package com.project.runningcrew.entity.runningrecords;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.Gps;
import com.project.runningcrew.entity.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "running_records")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "running_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RunningRecord extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "running_record_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @PositiveOrZero(message = "런닝 거리는 0 이상입니다.")
    @Column(nullable = false)
    private double runningDistance;

    @Positive(message = "런닝 시간은 1 이상입니다.")
    @Column(nullable = false)
    private int runningTime;

    @Positive(message = "런닝 페이스는 1 이상입니다.")
    @Column(nullable = false)
    private int runningFace;

    @Positive(message = "런닝 칼로리는 1 이상입니다.")
    @Column(nullable = false)
    private int calories;

    @NotNull
    @Size(max = 500, message = "런닝 기록 내용은 500 자 이하입니다.")
    @Column(nullable = false, length = 500)
    private String running_detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "runningRecord", cascade = CascadeType.ALL)
    private List<Gps> gpsList = new ArrayList<>();

    public RunningRecord(LocalDateTime startDateTime, double runningDistance, int runningTime,
                         int runningFace, int calories, String running_detail, User user) {
        this.startDateTime = startDateTime;
        this.runningDistance = runningDistance;
        this.runningTime = runningTime;
        this.runningFace = runningFace;
        this.calories = calories;
        this.running_detail = running_detail;
        this.user = user;
    }

}
