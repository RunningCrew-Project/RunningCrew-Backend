package com.project.runningcrew.entity.runningnotices;

import com.project.runningcrew.entity.BaseEntity;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "running_notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunningNotice extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "running_notice_id")
    private Long id;

    @NotBlank(message = "런닝 공지글 제목은 필수값입니다.")
    @Size(min = 1, max = 50, message = "런닝 공지글 제목은 1 자 이상 50 자 이하입니다.")
    @Column(nullable = false, length = 50)
    private String title;

    @NotBlank(message = "런닝 공지글 내용은 필수값입니다.")
    @Size(min = 1, max = 1000, message = "런닝 공지글 내용은 1 자 이상 1000 자 이하입니다.")
    @Column(nullable = false, length = 1000)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NoticeType noticeType;

    @Column(nullable = false)
    private LocalDateTime runningDateTime;

    @Positive(message = "런닝 인원은 1 이상입니다.")
    @Column(nullable = false)
    private int runningPersonnel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RunningStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_record_id")
    private RunningRecord preRunningRecord;

    @Builder
    public RunningNotice(Long id, String title, String detail, Member member, NoticeType noticeType,
                         LocalDateTime runningDateTime, int runningPersonnel, RunningStatus status,
                         RunningRecord preRunningRecord) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.member = member;
        this.noticeType = noticeType;
        this.runningDateTime = runningDateTime;
        this.runningPersonnel = runningPersonnel;
        this.status = status;
        this.preRunningRecord = preRunningRecord;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

    public void updateRunningTime(LocalDateTime runningTime) {
        this.runningDateTime = runningTime;
    }

    public void updateRunningPersonnel(int runningPersonnel) {
        this.runningPersonnel = runningPersonnel;
    }

    public void updateStatus(RunningStatus status) {
        this.status = status;
    }

    public void updatePreRunningRecord(RunningRecord preRunningRecord) {
        this.preRunningRecord = preRunningRecord;
    }

}
