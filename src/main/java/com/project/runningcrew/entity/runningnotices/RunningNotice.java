package com.project.runningcrew.entity.runningnotices;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.members.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunningNotice extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "running_notice_id")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    private LocalDateTime runningTime;

    @Positive
    private int runningPersonnel;

    @Enumerated(EnumType.STRING)
    private RunningStatus status;

    @Builder
    public RunningNotice(String detail, Member member, NoticeType noticeType, LocalDateTime runningTime,
                         int runningPersonnel, RunningStatus status) {

        this.detail = detail;
        this.member = member;
        this.noticeType = noticeType;
        this.runningTime = runningTime;
        this.runningPersonnel = runningPersonnel;
        this.status = status;

    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

    public void updateRunningTime(LocalDateTime runningTime) {
        this.runningTime = runningTime;
    }

    public void updateRunningPersonnel(int runningPersonnel) {
        this.runningPersonnel = runningPersonnel;
    }

    public void updateStatus(RunningStatus status) {
        this.status = status;
    }

}
