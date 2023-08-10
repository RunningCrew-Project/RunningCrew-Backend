package com.project.runningcrew.runningrecord.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SQLDelete(sql = "update running_records set deleted = true where running_record_id = ?")
@Where(clause = "deleted = false")
@Getter
@Table(name = "running_records")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "running_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RunningRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "running_record_id")
    private Long id;

    @NotBlank(message = "런닝기록 제목은 필수값입니다.")
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    private String location;

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
    private String runningDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "runningRecord", cascade = CascadeType.ALL)
    private List<Gps> gpsList = new ArrayList<>();

    @Column
    private boolean deleted = false;

    public RunningRecord(Long id, String title, LocalDateTime startDateTime, String location,
                         double runningDistance, int runningTime, int runningFace,
                         int calories, String runningDetail, User user) {
        this.id = id;
        this.title = title;
        this.startDateTime = startDateTime;
        this.location = location;
        this.runningDistance = runningDistance;
        this.runningTime = runningTime;
        this.runningFace = runningFace;
        this.calories = calories;
        this.runningDetail = runningDetail;
        this.user = user;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
